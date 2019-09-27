package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecGroupExample;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.SpecParamExample;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("ALL")
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> querySpecGroupByCategoryId(Long categoryId) {
        SpecGroupExample specGroupExample = new SpecGroupExample();
        specGroupExample.createCriteria().andCidEqualTo(categoryId);

        return specGroupMapper.selectByExample(specGroupExample);
    }

    public void updateSpecGroup(SpecGroup specGroup) {

        SpecGroupExample specGroupExample = new SpecGroupExample();
        SpecGroupExample.Criteria criteria = specGroupExample.createCriteria();
        criteria.andCidEqualTo(specGroup.getCid());
        criteria.andIdEqualTo(specGroup.getId());

        int updateByExample = specGroupMapper.updateByExample(specGroup, specGroupExample);

    }

    public void addSpecGroup(Long cid, String name) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        specGroup.setName(name);

        int insertSelective = specGroupMapper.insertSelective(specGroup);
    }

    public void deleteSpecGroup(Long id) {
        int deleteByPrimaryKey = specGroupMapper.deleteByPrimaryKey(id);
    }

    //根据条件查询规格参数
    public List<SpecParam> querySpecParamByGroupId(Long groupId, Long cid, Boolean generic, Boolean searching) {
        SpecParamExample specParamExample = new SpecParamExample();
        SpecParamExample.Criteria criteria = specParamExample.createCriteria();
        if (groupId != null) {
            criteria.andGroupIdEqualTo(groupId);
        }

        if (cid != null) {
            criteria.andCidEqualTo(cid);
        }

        if (generic != null) {
            criteria.andGenericEqualTo(generic);
        }

        if (searching != null) {
            criteria.andSearchingEqualTo(searching);
        }

        return specParamMapper.selectByExample(specParamExample);
    }

    public void addSpecParam(Long cid, Long groupId, String name, Boolean numeric, String unit, Boolean generic, Boolean searching, String segments) {

        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setGroupId(groupId);
        specParam.setName(name);
        specParam.setNumeric(numeric);
        specParam.setUnit(unit);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        specParam.setSegments(segments);

        int insertSelective = specParamMapper.insertSelective(specParam);
    }

    public void updateSpecParam(Long id, Long cid, Long groupId, String name, Boolean numeric, String unit, Boolean generic, Boolean searching, String segments) {

        SpecParam specParam = new SpecParam();
        specParam.setId(id);
        specParam.setCid(cid);
        specParam.setGroupId(groupId);
        specParam.setName(name);
        specParam.setNumeric(numeric);
        specParam.setUnit(unit);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        specParam.setSegments(segments);

        int updateByPrimaryKeySelective = specParamMapper.updateByPrimaryKeySelective(specParam);
    }

    public void deleteSpecParam(Long id) {
        int deleteByPrimaryKey = specParamMapper.deleteByPrimaryKey(id);
        System.out.println("deleteByPrimaryKey = " + deleteByPrimaryKey);
    }

    // 根据分类ID查询规格参数
    public List<SpecParam> querySpecParamByCid(Long cid) {
        SpecParamExample specParamExample = new SpecParamExample();
        specParamExample.createCriteria().andCidEqualTo(cid);

        return specParamMapper.selectByExample(specParamExample);
    }
}
