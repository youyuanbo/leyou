package com.leyou.item.service;

import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.StockExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockMapper stockMapper;

    // 根据skuId删除库存
    public void deleteStockBySkuId(List<Long> skuIdList) {
        StockExample stockExample = new StockExample();
        StockExample.Criteria criteria = stockExample.createCriteria();
        criteria.andSkuIdIn(skuIdList);

        stockMapper.deleteByExample(stockExample);
    }
}
