package com.leyou.search.pojo;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    // spuId
    @Id
    private Long id;

    //所有需要被搜索的信息，包含标题，分类，甚至品牌
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;

    // 子标题（不能使用此字段进行搜索，也不能使用此字段进行分词）
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;

    // 品牌ID
    private Long brandId;

    // 一级分类id
    private Long cid1;

    // 二级分类id
    private Long cid2;

    // 三级分类id
    private Long cid3;

    // spu创建时间
    private Date createtime;

    // 价格
    private Set<Long> price;

    //sku的信息（json结构）
    @Field(type = FieldType.Keyword, index = false)
    private String skus;

    // 可搜索的规格参数,key是参数名,值是参数值
    private Map<String, Object> specs;

}
