package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;

@Data
public class Sku {
    private Long id;

    private Long spuId;

    private String title;

    private String images;

    private Long price;

    private String indexes;

    private String ownSpec;

    private Boolean enable;

    private Date createTime;

    private Date lastUpdateTime;

    @Transient
    private Integer stock;// 库存
}