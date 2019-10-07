package com.leyou.item.service;

import com.leyou.common.vo.PageResult;
import com.leyou.vo.SpuVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void querySpuByPage() {
    //     PageResult<SpuVo> spuVoPageResult = goodsService.querySpuByPage("三", true, 1, 5);
    //     List<SpuVo> spuVoList = spuVoPageResult.getItems();
    //     System.out.println(spuVoList.size());
    //     spuVoList.forEach(System.out::println);
    }

    @Test
    public void saveGoods() {
    }

    @Test
    public void saveSkuAndStock() {
    }

    @Test
    public void querySpuDetailBySpuId() {
    }

    @Test
    public void querySkuListBySpuId() {
    }

    @Test
    public void updateGoods() {
    }

    @Test
    public void deleteGoods() {
        // goodsService.deleteGoods(201L);
        // goodsService.deleteGoods(202L);
        // goodsService.deleteGoods(204L);
    }
}