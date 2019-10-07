package com.leyou.item.controller;

import com.leyou.bo.SpuBo;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import com.leyou.vo.SpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    //查询商品
    @GetMapping(path = "spu/page")
    public ResponseEntity<PageResult<SpuVo>> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        return ResponseEntity.ok(goodsService.querySpuByPage(key, saleable, page, rows));
    }

    // 添加商品
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo) {
        goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 根据spu_id查询spu_detail表
    @GetMapping(path = "spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable Long spuId) {
        return ResponseEntity.ok(goodsService.querySpuDetailBySpuId(spuId));
    }

    // 根据spu_id查询所有的Sku
    @GetMapping(path = "sku/list/{spuId}")
    public ResponseEntity<List<Sku>> querySkuListBySpuId(@PathVariable Long spuId) {
        return ResponseEntity.ok(goodsService.querySkuListBySpuId(spuId));
    }

    // 更新商品
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo) {
        goodsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("goods")
    public ResponseEntity<Void> deleteGoods(@PathVariable Long spuId) {
        goodsService.deleteGoods(spuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("spu/{spuId}")
    public ResponseEntity<Spu> querySpuById(@PathVariable Long spuId) {
        return ResponseEntity.ok(goodsService.querySpuIdBySpuId(spuId));
    }

}
