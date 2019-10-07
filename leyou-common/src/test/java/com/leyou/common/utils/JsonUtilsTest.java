package com.leyou.common.utils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonUtilsTest {


    @Test
    public void serialize() {

        // List<String> list = new ArrayList<>();
        // list.add("xiaoyou");
        // list.add("xiaochen");
        //
        // System.out.println(JsonUtils.serialize(list));

    }

    @Test
    public void parse() {
    }

    @Test
    public void parseList() {
        // List<String> list = JsonUtils.parseList("[\"xiaoyou\",\"xiaochen\"]", String.class);
        //
        // list.forEach(System.out::println);
    }

    @Test
    public void parseMap() {

        // String user = "{\"name\":\"xiaoyou\",\"age\":\"22\"}";
        //
        // Map<String, String> map = JsonUtils.parseMap(user, String.class, String.class);
        //
        // for (Map.Entry<String, String> entry : map.entrySet()) {
        //     System.out.println(entry.getKey());
        //     System.out.println(entry.getValue());
        // }
    }

    @Test
    public void nativeRead() {
    }
}