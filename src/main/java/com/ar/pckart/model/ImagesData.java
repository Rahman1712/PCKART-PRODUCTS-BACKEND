package com.ar.pckart.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagesData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imageid;
	
	private Long productId;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "product_main_image", joinColumns = {
			@JoinColumn(name = "image_id")
			},
				inverseJoinColumns = {
						@JoinColumn(name = "main_image_id")
				}
			) 
	ImageModel productMainImage;
	
	@ManyToMany(fetch = FetchType.EAGER, 
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinTable(name = "product_sub_images",
			joinColumns = {
					@JoinColumn(name = "image_id")
			},
				inverseJoinColumns = {
						@JoinColumn(name = "sub_image_id")
				}
			)
	Set<ImageModel> productSubImages;
	
}
