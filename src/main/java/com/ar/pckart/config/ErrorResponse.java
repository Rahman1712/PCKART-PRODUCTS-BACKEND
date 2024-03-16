package com.ar.pckart.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private String message;
	private int status;
	private String timestamp;
	private String path;
}
/*
 * {
    "errorMessage": "{\"timestamp\":\"2023-07-01T19:20:11.569+00:00\",
    \"status\":400,\"error\":\"Bad Request\",\"message\":\"Bad Request to Products Service\",\"path\":\"/products/pckart/api/v1/products/auth/add-product-imgs\"}"
}
 * 
 */
