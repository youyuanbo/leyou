package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping(path = "groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupByCategoryId(@PathVariable("cid") Long cid) {
        List<SpecGroup> specGroupList = specificationService.querySpecGroupByCategoryId(cid);
        return ResponseEntity.ok(specGroupList);
    }

    @PutMapping(path = "group")
    public ResponseEntity<Void> updateSpecGroup(SpecGroup specGroup) {
        specificationService.updateSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(path = "group")
    public ResponseEntity<Void> addSpecGroup(
            @RequestParam("cid") Long cid,
            @RequestParam("name") String name
    ) {

        specificationService.addSpecGroup(cid, name);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping(path = "group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id") Long id) {
        specificationService.deleteSpecGroup(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(path = "params/{groupId}")
    public ResponseEntity<List<SpecParam>> querySpecParamsByGroupId(@PathVariable("groupId") Long groupId) {
        List<SpecParam> specParamList = specificationService.querySpecParamByGroupId(groupId);
        return ResponseEntity.ok(specParamList);
    }

    @PostMapping(path = "param")
    public ResponseEntity<Void> addSpecParam(
            @RequestParam("cid") Long cid,
            @RequestParam("groupId") Long groupId,
            @RequestParam("name") String name,
            @RequestParam("numeric") Boolean numeric,
            @RequestParam("unit") String unit,
            @RequestParam("generic") Boolean generic,
            @RequestParam("searching") Boolean searching,
            @RequestParam("segments") String segments
    ) {

        specificationService.addSpecParam(cid, groupId, name, numeric, unit, generic, searching, segments);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(path = "param")
    public ResponseEntity<Void> updateSpecParam(
            @RequestParam("id") Long id,
            @RequestParam("cid") Long cid,
            @RequestParam("groupId") Long groupId,
            @RequestParam("name") String name,
            @RequestParam("numeric") Boolean numeric,
            @RequestParam("unit") String unit,
            @RequestParam("generic") Boolean generic,
            @RequestParam("searching") Boolean searching,
            @RequestParam("segments") String segments
    ) {
        specificationService.updateSpecParam(id, cid, groupId, name, numeric, unit, generic, searching, segments);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(path = "param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable(name = "id") Long id) {
        specificationService.deleteSpecParam(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
