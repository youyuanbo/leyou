package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeYouException;
import com.leyou.item.mapper.CategoryBrandMapper;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.CategoryBrandExample;
import com.leyou.item.pojo.CategoryBrandKey;
import com.leyou.item.pojo.CategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatrgoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentIdEqualTo(pid);

        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new LeYouException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }

        return categoryList;
    }

    public List<String> queryCategoryIdListByBid(Long bid) {

        CategoryBrandExample categoryBrandExample = new CategoryBrandExample();
        CategoryBrandExample.Criteria criteria = categoryBrandExample.createCriteria();
        criteria.andBrandIdEqualTo(bid);

        //根据品牌ID查询tb_category_brand表，提取查询结果中的categoryId
        List<Long> categoryIdList = categoryBrandMapper.selectByExample(categoryBrandExample).stream().map(CategoryBrandKey::getCategoryId).collect(Collectors.toList());

        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria categoryExampleCriteria = categoryExample.createCriteria();
        categoryExampleCriteria.andIdIn(categoryIdList);
        //根据categoryId查询tb_category表，提取查询结果中的categoryName
        return categoryMapper.selectByExample(categoryExample).stream().map(Category::getName).collect(Collectors.toList());
    }

    public List<String> queryNameByIds(List<Long> ids) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andIdIn(ids);

        return categoryMapper.selectByExample(categoryExample).stream().map(Category::getName).collect(Collectors.toList());
}
}
