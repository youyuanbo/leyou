package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "rows", defaultValue = "5") Integer rows,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "desc", defaultValue = "false") Boolean desc
    ) {
        PageResult<Brand> brandPageResult = brandService.queryBrandByPage(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(brandPageResult);
    }

    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        brandService.updateBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBrand(@RequestParam("bid") Long bid) {
        brandService.deleteBrand(bid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable(name = "cid") Long cid) {
        return ResponseEntity.ok(brandService.queryBrandByCid(cid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.queryBrandById(id));
    }

    @GetMapping("/brandList")
    public ResponseEntity<List<Brand>> queryBrandListByBidList(@RequestParam List<Long> brandIdList) {
        List<Brand> brandList = brandService.queryBrandListByBidList(brandIdList);
        if (CollectionUtils.isEmpty(brandIdList)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(brandList);
    }

}
