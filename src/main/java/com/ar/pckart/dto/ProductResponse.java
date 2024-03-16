package com.ar.pckart.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ProductResponse {

	private Long productId;
	private String productName;
	private double productPrice;
//	private int productQuantity; 
	private Long productQuantity; 
	private float productDiscount;
	private String productColor;
	
	private String productDescription;
	private List<Map<String,String>> productSpecs;
	
	private Long brandId;
	private String brandName;
	
	private Long categoryId;
	private String categoryName;
	
	private LocalDateTime added_at;
	
	private boolean active;
	

	public ProductResponse(Long productId, String productName, 
			double productPrice, 
			//int productQuantity, 
			Long productQuantity, 
			float productDiscount,
			String productColor, String productDescription, 
			Long brandId, String brandName,
			Long categoryId, String categoryName, 
			LocalDateTime added_at, boolean active) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productQuantity = productQuantity;
		this.productDiscount = productDiscount;
		this.productColor = productColor;
		this.productDescription = productDescription;
		this.brandId = brandId;
		this.brandName = brandName;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.added_at = added_at;
		this.active = active;
	}


}
