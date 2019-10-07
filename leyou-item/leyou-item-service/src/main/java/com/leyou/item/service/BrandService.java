package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeYouException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.CategoryBrandMapper;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.BrandExample;
import com.leyou.item.pojo.CategoryBrandExample;
import com.leyou.item.pojo.CategoryBrandKey;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        //分页
        PageHelper.startPage(page, rows);

        //过滤
        BrandExample brandExample = new BrandExample();

        if (StringUtils.isNotBlank(key)) {
            brandExample.or().andNameLike("%" + key + "%");
            brandExample.or().andLetterLike("%" + key.toUpperCase() + "%");
        }

        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            brandExample.setOrderByClause(sortBy + (desc ? " DESC" : " ASC"));
        }

        // 查询
        List<Brand> brandList = brandMapper.selectByExample(brandExample);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new LeYouException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPageSize(), brandList);
    }

    //新增品牌
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //向tb_brand表中插入一个品牌
        brand.setId(null);
        int insertBrandResult = brandMapper.insertSelective(brand);
        if (insertBrandResult != 1) {
            throw new LeYouException(ExceptionEnum.BRAND_SAVE_ERROR);
        }

        //查询此品牌的ID
        BrandExample brandExample = new BrandExample();
        BrandExample.Criteria brandExampleCriteria = brandExample.createCriteria();
        brandExampleCriteria.andNameEqualTo(brand.getName());

        Long brandId = brandMapper.selectByExample(brandExample).get(0).getId();

        //向tb_category_brand中间表插入分类与品牌的关系
        for (Long cid : cids) {
            CategoryBrandKey categoryBrandKey = new CategoryBrandKey();
            categoryBrandKey.setBrandId(brandId);
            categoryBrandKey.setCategoryId(cid);
            int insertCategoryResult = categoryBrandMapper.insertSelective(categoryBrandKey);
            if (insertCategoryResult != 1) {
                throw new LeYouException(ExceptionEnum.CATEGORY_BRAND_SAVE_ERROR);
            }
        }
    }

    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {

        // 根据品牌ID更新品牌表

        int updateBrandResult = brandMapper.updateByPrimaryKeySelective(brand);

        // 根据品牌id删除tb_category_brand中间表的数据
        CategoryBrandExample categoryBrandExample = new CategoryBrandExample();
        categoryBrandExample.createCriteria().andBrandIdEqualTo(brand.getId());
        int deleteCategoryBrandResult = categoryBrandMapper.deleteByExample(categoryBrandExample);

        // 向tb_category_brand中间表插入分类与品牌的关系
        for (Long cid : cids) {
            CategoryBrandKey categoryBrandKey = new CategoryBrandKey();
            categoryBrandKey.setCategoryId(cid);
            categoryBrandKey.setBrandId(brand.getId());
            int insertCategoryBrandResult = categoryBrandMapper.insert(categoryBrandKey);
        }
    }

    @Transactional
    public void deleteBrand(Long bid) {
        // 根据品牌id删除tb_brand中的数据

        int deleteBrandResult = brandMapper.deleteByPrimaryKey(bid);

        // 根据品牌Id删除tb_category_brand表中的数据
        CategoryBrandExample categoryBrandExample = new CategoryBrandExample();
        categoryBrandExample.createCriteria().andBrandIdEqualTo(bid);

        int deleteCategoryBrandResult = categoryBrandMapper.deleteByExample(categoryBrandExample);
    }

    public String queryBrandNameById(Long id) {
        return brandMapper.selectByPrimaryKey(id).getName();
    }

    public Brand queryBrandById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    //根据分类ID查询品牌
    public List<Brand> queryBrandByCid(Long cid) {
        // 根据分类id，查询tb_category_brand表，获取品牌ID
        CategoryBrandExample categoryBrandExample = new CategoryBrandExample();
        CategoryBrandExample.Criteria categoryBrandExampleCriteria = categoryBrandExample.createCriteria();
        categoryBrandExampleCriteria.andCategoryIdEqualTo(cid);
        List<Long> brandIdList = categoryBrandMapper.selectByExample(categoryBrandExample)
                .stream().map(CategoryBrandKey::getBrandId)
                .collect(Collectors.toList());
        // 根据品牌Id列表，查询品牌
        BrandExample brandExample = new BrandExample();
        BrandExample.Criteria brandExampleCriteria = brandExample.createCriteria();
        brandExampleCriteria.andIdIn(brandIdList);

        return brandMapper.selectByExample(brandExample);
    }

    public List<Brand> queryBrandListByBidList(List<Long> brandIdList) {

        BrandExample brandExample = new BrandExample();
        BrandExample.Criteria criteria = brandExample.createCriteria();
        criteria.andIdIn(brandIdList);

        List<Brand> brandList = brandMapper.selectByExample(brandExample);

        if (CollectionUtils.isEmpty(brandList)) {
            throw new LeYouException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        return brandList;
    }
}
