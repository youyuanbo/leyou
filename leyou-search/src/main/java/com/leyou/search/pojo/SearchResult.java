package com.leyou.search.pojo;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data

public class SearchResult extends PageResult<Goods> {
    private List<Category> categoryList;

    private List<Brand> brandList;

    private List<Map<String, Object>> specs;

    public SearchResult() {
    }

    public SearchResult(Long total, Integer pages, List<Goods> items, List<Category> categoryList, List<Brand> brandList) {
        super(total, pages, items);
        this.categoryList = categoryList;
        this.brandList = brandList;
    }

    public SearchResult(Long total, Integer pages, List<Goods> items, List<Category> categoryList, List<Brand> brandList, List<Map<String, Object>> specs) {
        super(total, pages, items);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.specs = specs;
    }
}

