package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnum;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author youyuanbo
 */
@Data
public class ExceptionResult {

	private Integer code;

	private String message;

	private String time;

	public ExceptionResult(ExceptionEnum exceptionEnum) {
		this.code = exceptionEnum.getCode();
		this.message = exceptionEnum.getMessage();
		this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:SSS").format(new Date());
	}
}
