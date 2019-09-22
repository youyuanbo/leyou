package com.leyou.item.mapper;

import com.leyou.item.pojo.CategoryBrandExample;
import com.leyou.item.pojo.CategoryBrandKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CategoryBrandMapper {
    long countByExample(CategoryBrandExample example);

    int deleteByExample(CategoryBrandExample example);

    int deleteByPrimaryKey(CategoryBrandKey key);

    int insert(CategoryBrandKey record);

    int insertSelective(CategoryBrandKey record);

    List<CategoryBrandKey> selectByExample(CategoryBrandExample example);

    int updateByExampleSelective(@Param("record") CategoryBrandKey record, @Param("example") CategoryBrandExample example);

    int updateByExample(@Param("record") CategoryBrandKey record, @Param("example") CategoryBrandExample example);
}