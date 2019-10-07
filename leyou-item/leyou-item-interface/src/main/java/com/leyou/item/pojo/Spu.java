package com.leyou.item.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Spu {
    private Long id;

    private String title;

    private String subTitle;

    private Long cid1;

    private Long cid2;

    private Long cid3;

    private Long brandId;

    private Boolean saleable;

    private Boolean valid;

    private Date createTime;

    private Date lastUpdateTime;

    @Transient
    private List<Sku> skuList;

    @Transient
    private SpuDetail spuDetail;


}