package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeYouException;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuService {
    @Autowired
    private SpuMapper spuMapper;

    public void deleteSpuById(Long spuId) {
        spuMapper.deleteByPrimaryKey(spuId);
    }

    public Spu querySpuBySpuId(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if (spu == null) {
            throw new LeYouException(ExceptionEnum.SPU_NOT_FOUND);
        }
        return spu;
    }
}
