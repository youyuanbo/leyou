package com.leyou.api;


import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {

    @GetMapping("/category/list/ids")
    List<String> queryCategoryNameByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("/category/list/categoryIds")
    List<Category> queryCategoryListByCategoryIdList(@RequestParam List<Long> categoryIdList);
}

