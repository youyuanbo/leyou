package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpecificationServiceTest {

    @Autowired
    private SpecificationService specificationService;

    @Test
    public void querySpecGroupByCategoryId() {
        //
        // List<SpecGroup> specGroupList = specificationService.querySpecGroupByCategoryId(76L);
        // System.out.println(specGroupList.size());

    }

    @Test
    public void updateSpecGroup() {

        // SpecGroup specGroup = new SpecGroup();
        // specGroup.setId(1L);
        // specGroup.setCid(76L);
        // specGroup.setName("主体");
        // specificationService.updateSpecGroup(specGroup);
    }

    @Test
    public void addSpecGroup() {
        // specificationService.addSpecGroup(76L, "新增1");
    }

    @Test
    public void deleteSpecGroup() {
        // specificationService.deleteSpecGroup(28L);
    }

    @Test
    public void querySpecParamByGroupId() {
        // List<SpecParam> specParamList = specificationService.querySpecParamByGroupId(1L);
        // System.out.println("specParamList.size() = " + specParamList.size());
    }

    @Test
    public void addSpecParam() {
        // SpecParam specParam = new SpecParam();
        // specParam.setCid(76L);
        // specParam.setGroupId(1L);
        // specParam.setName("手机-主体-新增-测试");
        // specParam.setNumeric(true);
        // specParam.setUnit("g");
        // specParam.setGeneric(true);
        // specParam.setSearching(true);
        // specParam.setSegments("1-10,11-20");
        // specificationService.addSpecParam(specParam);
    }

    @Test
    public void updateSpecParam() {


    }
}