package com.leyou.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private Long total;

    private Integer pages;

    private List<T> items;

    public PageResult() {
    }

    public PageResult(Integer pages, List<T> items) {
        this.pages = pages;
        this.items = items;
    }

    public PageResult(Long total, Integer pages, List<T> items) {
        this.total = total;
        this.pages = pages;
        this.items = items;
    }
}
