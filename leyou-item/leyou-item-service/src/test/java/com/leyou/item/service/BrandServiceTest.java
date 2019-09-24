package com.leyou.item.service;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BrandServiceTest {
    @Autowired
    private BrandService brandService;

    @Test
    public void queryBrandByPage() {
    }

    @Test
    public void saveBrand() {
    }

    @Test
    public void queryCategoryListByBid() {
        // List<Long> categoryIdList = brandService.queryCategoryListByBid(18374L);
        // System.out.println(categoryIdList.size());
    }

}