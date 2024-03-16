package com.ar.pckart.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ItemExceptionHandlerController {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public Map<String, String> handleBuisnessException(SQLIntegrityConstraintViolationException ex){
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("message", ex.getMessage());
		errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()+"");
		return errorMap;
	}
}
