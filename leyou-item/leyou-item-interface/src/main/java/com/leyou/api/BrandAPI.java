package com.leyou.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrandAPI {

    @GetMapping("/brand/{id}")
    Brand queryBrandById(@PathVariable Long id);

    @GetMapping("/brand/brandList")
    List<Brand> queryBrandListByBidList(@RequestParam List<Long> brandIdList);
}
