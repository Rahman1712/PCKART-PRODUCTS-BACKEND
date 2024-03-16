package com.ar.pckart.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class ProductDetails {

	private Long id;
	private String name;
	private String brand;
	private double price;
//	private int quantity;
	private Long quantity;
	private float discount;
	private String category;
	private String color;
}
