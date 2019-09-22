package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author youyuanbo
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptionEnum {

    //价格不能为空
    PRICE_CANNOT_BE_NULL(400, "价格不能为空"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "商品分类没有找到"),
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "品牌没有找到"),
    GOODS_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "商品不存在"),
    GOODS_SKU_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "商品SKU不存在"),
    GOODS_STOCK_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "商品库存不存在"),
    GOODS_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "商品不存在"),
    GOODS_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "商品保存失败"),
    GOODS_UPDATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "商品更新失败"),
    GOODS_ID_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST.value(), "商品ID不能为空"),
    BRAND_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "新增品牌失败"),
    CATEGORY_BRAND_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "新增分类品牌表失败"),
    UPLOAD_FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "文件上传失败"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST.value(), "无效的文件类型"),
    SPEC_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "商品规格组没找到"),
    SPEC_PARAM_NOT_FOUNT(HttpStatus.NOT_FOUND.value(), "商品规格参数不存在"),

    ;

    private Integer code;

    private String message;


}
