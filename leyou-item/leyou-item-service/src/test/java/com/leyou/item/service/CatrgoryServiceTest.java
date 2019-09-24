package com.leyou.item.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CatrgoryServiceTest {

    @Autowired
    private CatrgoryService catrgoryService;

    @Test
    public void queryCategoryListByPid() {
        // List<Category> categoryList = catrgoryService.queryCategoryListByPid(0L);
        // System.out.println(categoryList.size());
    }

    @Test
    public void queryCategoryIdListByBid() {
        // List<Long> categoryIdList = catrgoryService.queryCategoryIdListByBid(18374L);
        // System.out.println(categoryIdList.size());

    }


}