package com.ar.pckart.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banners")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Banner {
	
	@Id
	@GeneratedValue( strategy = GenerationType.UUID)
	@GenericGenerator(name = "uuid", type = UuidGenerator.class)
	private String id;

    private String bannerHeader;

    private String description1;

    private String description2;

	@Lob 
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "banner_image",length=100000)
    private byte[] bannerImage;
	
	
	@Column(name = "banner_image_name")
	private String imageName;
	
	@Column(name = "banner_image_type")
	private String imageType;
 
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product; 

    private boolean enabled=true;
}