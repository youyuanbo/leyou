package com.leyou.item.service;

import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.pojo.SpuDetailExample;
import com.leyou.item.pojo.SpuExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuDetailService {

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    // 根据SpuId删除spuDetail
    public void deleteSpuDetailBySpuId(Long spuId) {
        SpuDetailExample spuDetailExample = new SpuDetailExample();
        SpuDetailExample.Criteria criteria = spuDetailExample.createCriteria();
        criteria.andSpuIdEqualTo(spuId);

        spuDetailMapper.deleteByExample(spuDetailExample);

    }
}
