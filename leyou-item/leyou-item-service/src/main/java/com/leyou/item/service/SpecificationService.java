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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("ALL")
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    // 根据分类Id查询规格组
    public List<SpecGroup> querySpecGroupByCategoryId(Long categoryId) {
        SpecGroupExample specGroupExample = new SpecGroupExample();
        specGroupExample.createCriteria().andCidEqualTo(categoryId);

        return specGroupMapper.selectByExample(specGroupExample);
    }

    // 根据分类ID查询规格组及规格组对应的参数
    public List<SpecGroup> querySpecGroupAndSpecParamByCategoryId(Long categoryId) {
        //封装查询条件，根据分类Id，查询规格组
        SpecGroupExample specGroupExample = new SpecGroupExample();
        SpecGroupExample.Criteria criteria = specGroupExample.createCriteria();
        criteria.andCidEqualTo(categoryId);
        List<SpecGroup> specGroupList = specGroupMapper.selectByExample(specGroupExample);

        // 根据分类Id，查询所有的规格参数
        List<SpecParam> specParamList = querySpecParamByCid(categoryId);

        // 将所有的规格参数，转换到map中，map的key是规格参数组id，value是规则参数
        Map<Long, List<SpecParam>> specParamMap = new HashMap<>();
        specParamList.forEach(specParam -> {
            // 如果map中还不存在当前规则参数，则添加一个arrayList
            if (!specParamMap.containsKey(specParam.getGroupId())) {
                specParamMap.put(specParam.getGroupId(), new ArrayList<>());
            }
            // 如果已经存在，则把规格参数添加到map中
            specParamMap.get(specParam.getGroupId()).add(specParam);
        });

        // 将规格参数添加到规格参数组中
        specGroupList.forEach(specGroup -> specGroup.setSpecParamList(specParamMap.get(specGroup.getId())));
        // 返回规格参数组
        return specGroupList;
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

    //新增规格参数
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

    // 更新规格参数
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

    //根据规格参数Id，删除规格参数
    public void deleteSpecParam(Long id) {
        int deleteByPrimaryKey = specParamMapper.deleteByPrimaryKey(id);
    }

    // 根据分类ID查询规格参数
    public List<SpecParam> querySpecParamByCid(Long cid) {
        SpecParamExample specParamExample = new SpecParamExample();
        specParamExample.createCriteria().andCidEqualTo(cid);

        return specParamMapper.selectByExample(specParamExample);
    }
}
