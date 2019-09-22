package com.leyou.common.advice;

import com.leyou.common.exception.LeYouException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author youyuanbo
 */
@ControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(LeYouException.class)
	public ResponseEntity<ExceptionResult> handleException(LeYouException e) {
		return ResponseEntity
				.status(e.getExceptionEnum().getCode())
				.body(new ExceptionResult(e.getExceptionEnum()));
	}
}
