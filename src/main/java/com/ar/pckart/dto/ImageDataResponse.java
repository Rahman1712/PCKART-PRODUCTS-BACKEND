package com.ar.pckart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDataResponse {

	private Long id;
	private Long parent_id;
	private String main_img_path;
	private String main_img_type;
	
	private String img1path;
	private String img1type;
	
	private String img2path;
	private String img2type;
	
	private String img3path;
	private String img3type;
	
	private String img4path;
	private String img4type;
}
