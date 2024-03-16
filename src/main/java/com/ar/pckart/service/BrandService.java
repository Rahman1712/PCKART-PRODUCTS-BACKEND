package com.ar.pckart.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ar.pckart.dto.BrandResponse;
import com.ar.pckart.model.Brand;
import com.ar.pckart.repo.BrandRepo;
import com.ar.pckart.util.ImageUtils;

@Service
public class BrandService {

	@Autowired
	private BrandRepo brandRepo;
	
	public Brand addNew(MultipartFile file, String name) throws IOException {
		Brand brand = new Brand();
		brand.setName(name);
		brand.setImage(ImageUtils.compress(file.getBytes()));
		brand.setImageName(file.getOriginalFilename());
		brand.setImageType(file.getContentType());

		return brandRepo.save(brand);
	}

	public void update(Long id, String name, MultipartFile file) throws IOException {
		brandRepo.updateBrand(id, name, 
				ImageUtils.compress(file.getBytes()),
				file.getOriginalFilename(),
				file.getContentType()
				);
	}

	public void deleteById(Long id) {
		brandRepo.deleteById(id);
	}

	public Brand getById(Long id) throws IOException {
		Brand brand = brandRepo.findById(id).get();
		brand.setImage(ImageUtils.decompress(brand.getImage()));
		return brand;
	}

	public List<Brand> allBrands() {
		//List<Brand> allBrands = brandRepo.findAll(); 
		List<Brand> allBrands = brandRepo.findAllByOrderByAddedatDesc(); 
		allBrands.forEach(brand -> {
				brand.setImage(ImageUtils.decompress(brand.getImage()));
		});
		return allBrands;
	}

	public List<BrandResponse> allBrandsIdAndName() {
		return brandRepo.allBrandsIdAndName();
	}

	public Optional<BrandResponse> getByIdResponse(Long id) {
		return brandRepo.findByIdResponse(id);
	}

}

/*
 * public Brand save(Brand brand) { return brandRepo.save(brand); }
 * 
 * 
 */
