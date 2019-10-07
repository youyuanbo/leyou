package com.leyou.item.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.List;

@Data
@NoArgsConstructor
public class SpecGroup {
    private Long id;

    private Long cid;

    private String name;

    @Transient
    private List<SpecParam> specParamList;
}