package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CatrgoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "category")
public class CagetoryController {

    @Autowired
    private CatrgoryService catrgoryService;

    @GetMapping(path = "list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid) {
        return ResponseEntity.ok(catrgoryService.queryCategoryListByPid(pid));
    }
}
