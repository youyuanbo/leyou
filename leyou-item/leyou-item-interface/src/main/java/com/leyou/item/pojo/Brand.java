package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;

@Data
public class Brand {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String name;

    @Transient
    private String image;

    private String letter;

}