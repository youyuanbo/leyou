package com.leyou.item.service;

import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SkuExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuService {

    @Autowired
    private SkuMapper skuMapper;

    // 根据spuId查询所有的Sku
    public List<Sku> querySkuListBySpuId(Long spuId) {
        SkuExample skuExample = new SkuExample();
        SkuExample.Criteria criteria = skuExample.createCriteria();
        criteria.andSpuIdEqualTo(spuId);

        return skuMapper.selectByExample(skuExample);
    }

    // 批量删除sku
    public void patchDeleteSku(List<Long> skuIdList) {
        SkuExample skuExample = new SkuExample();
        SkuExample.Criteria criteria = skuExample.createCriteria();
        criteria.andIdIn(skuIdList);
        skuMapper.deleteByExample(skuExample);
    }

}
