package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.bo.SpuBo;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import com.leyou.vo.SpuVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CatrgoryService catrgoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StockService stockService;

    @Autowired
    private SpuDetailService spuDetailService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    // 查询商品
    public PageResult<SpuVo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {

        SpuExample spuExample = new SpuExample();
        SpuExample.Criteria criteria = spuExample.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andTitleLike("%" + key + "%");
        }

        if (saleable != null) {
            criteria.andSaleableEqualTo(saleable);
        }

        PageHelper.startPage(page, rows);

        List<Spu> spuList = spuMapper.selectByExample(spuExample);
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spuList);

        List<SpuVo> spuVoList = new ArrayList<>();
        spuList.forEach(spu -> {
            SpuVo spuVo = new SpuVo();
            BeanUtils.copyProperties(spu, spuVo);

            List<String> nameList = catrgoryService.queryCategoryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuVo.setCname(StringUtils.join(nameList, "/"));

            spuVo.setBname(brandService.queryBrandNameById(spu.getBrandId()));

            spuVoList.add(spuVo);

        });

        return new PageResult<>(spuPageInfo.getTotal(), spuPageInfo.getPages(), spuVoList);
    }

    //新增商品
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        spuMapper.insertSelective(spuBo);


        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());

        spuDetailMapper.insertSelective(spuDetail);

        saveSkuAndStock(spuBo);
        // 发送mq消息
        amqpTemplate.convertAndSend("item.insert", spuBo.getId());

    }

    //新增库存与sku
    public void saveSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {

            // 新增sku
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            // 新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    // 根据spu_id查询spu_detail表
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    // 根据spu_id查询所有的Sku
    public List<Sku> querySkuListBySpuId(Long spuId) {
        SkuExample skuExample = new SkuExample();
        SkuExample.Criteria criteria = skuExample.createCriteria();
        criteria.andSpuIdEqualTo(spuId);

        return skuMapper.selectByExample(skuExample);
    }

    // 更新goods
    public void updateGoods(SpuBo spuBo) {

        //根据spu_id查询SKU集合
        List<Sku> skuList = querySkuListBySpuId(spuBo.getId());
        if (!CollectionUtils.isEmpty(skuList)) {
            // 提取所有sku的id
            List<Long> skuIdList = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            //根据skuId删除库存
            StockExample stockExample = new StockExample();
            StockExample.Criteria criteria = stockExample.createCriteria();
            criteria.andSkuIdIn(skuIdList);
            stockMapper.deleteByExample(stockExample);
            // 根据spuId删除sku
            SkuExample skuExample = new SkuExample();
            SkuExample.Criteria skuExampleCriteria = skuExample.createCriteria();
            skuExampleCriteria.andSpuIdEqualTo(spuBo.getId());

            skuMapper.deleteByExample(skuExample);
        }
        //重新添加库存与sku
        saveSkuAndStock(spuBo);


        spuBo.setLastUpdateTime(new Date());
        spuBo.setCreateTime(null);
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        //更新spu
        spuMapper.updateByPrimaryKeySelective(spuBo);
        //更新spuDetail
        spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
        // 发送mq消息
        amqpTemplate.convertAndSend("item.updata", spuBo.getId());
    }

    // 删除商品
    public void deleteGoods(Long spuId) {

        // spu, spu_detail, sku, stock
        // 根据spuId查询SkuList
        List<Sku> skuList = skuService.querySkuListBySpuId(spuId);
        if (!CollectionUtils.isEmpty(skuList)) {
            //如果存在sku，并且存在库存，删除库存
            List<Long> skuIdList = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockService.deleteStockBySkuId(skuIdList);

            // 删除sku
            skuService.patchDeleteSku(skuIdList);
        }
        // 根据spuId删除SpuDetail
        spuDetailService.deleteSpuDetailBySpuId(spuId);
        // 根据spuId删除Spu
        spuService.deleteSpuById(spuId);
        // 发送mq消息
        amqpTemplate.convertAndSend("item.delete", spuId);
    }

    // 根据spuId查询Spu
    public Spu querySpuIdBySpuId(Long spuId) {
        // 查询spu
        Spu spu = spuService.querySpuBySpuId(spuId);

        spu.setSkuList(querySkuListBySpuId(spuId));

        spu.setSpuDetail(querySpuDetailBySpuId(spuId));

        return spu;
    }
}
