package com.leyou.item.service;

import com.leyou.item.mapper.SpuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuService {
    @Autowired
    private SpuMapper spuMapper;

    public void deleteSpuById(Long spuId) {
        spuMapper.deleteByPrimaryKey(spuId);
    }
}
