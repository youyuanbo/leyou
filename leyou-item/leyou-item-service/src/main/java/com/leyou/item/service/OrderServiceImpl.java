package com.leyou.item.service;

import com.leyou.item.mapper.OrderMapper;
import com.leyou.item.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order queryById(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }
}
