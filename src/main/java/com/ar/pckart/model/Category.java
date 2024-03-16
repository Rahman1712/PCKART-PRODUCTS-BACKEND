package com.ar.pckart.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@JsonAlias({"name","category_name","categoryName"})
	@Column(name = "category_name", unique = true, nullable = false)
	private String name;
	
	@Lob 
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "category_image",length=100000)
	private byte[] image;
	
	@Column(name = "category_image_name")
	private String imageName;
	
	@Column(name = "category_image_type")
	private String imageType;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@Column(name = "added_at", 
			nullable = false, updatable = false, insertable = false,
			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime addedat;

	@JsonIgnore
	@OneToMany(mappedBy = "parent" , cascade = CascadeType.ALL)
	private Set<Category> children;
	
} 
