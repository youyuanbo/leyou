package com.leyou.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationAPI {

    @GetMapping(path = "/spec/params")
    List<SpecParam> querySpecParamsByGroupId(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "searching", required = false) Boolean searching
    );

    @GetMapping(path = "spec/group")
    List<SpecGroup> querySpecGroupAndSpecParamByCategoryId(@RequestParam Long cid);
}
