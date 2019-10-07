package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CatrgoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "category")
public class CategoryController {

    @Autowired
    private CatrgoryService catrgoryService;

    @GetMapping(path = "list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid) {
        return ResponseEntity.ok(catrgoryService.queryCategoryListByPid(pid));
    }

    @GetMapping(path = "bid/{bid}")
    public ResponseEntity<List<String>> queryCategoryIdListByBid(@PathVariable("bid") Long bid) {
        return ResponseEntity.ok(catrgoryService.queryCategoryIdListByBid(bid));
    }

    @GetMapping("list/ids")
    public ResponseEntity<List<String>> queryCategoryNameByIds(@RequestParam List<Long> ids) {

        List<String> categoryNameList = catrgoryService.queryCategoryNameByIds(ids);
        if (CollectionUtils.isEmpty(categoryNameList)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categoryNameList);
    }

    @GetMapping("/list/categoryIds")
    public ResponseEntity<List<Category>> queryCategoryListByCategoryIdList(@RequestParam List<Long> categoryIdList) {

        return ResponseEntity.ok(catrgoryService.queryCategoryListByCategoryIdList(categoryIdList));
    }

}
