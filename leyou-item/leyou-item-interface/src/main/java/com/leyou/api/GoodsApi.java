package com.leyou.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.vo.SpuVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    @GetMapping("spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable Long id);

    @GetMapping(path = "sku/list/{spuId}")
    List<Sku> querySkuListBySpuId(@PathVariable Long spuId);

    @GetMapping(path = "spu/page")
    PageResult<SpuVo> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

    @GetMapping("spu/{spuId}")
    Spu querySpuById(@PathVariable("spuId") Long SpuId);

}
