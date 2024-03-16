 package com.ar.pckart.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.pckart.dto.BrandResponse;
import com.ar.pckart.model.Brand;
import com.ar.pckart.service.BrandService;

@RestController
@RequestMapping("/pckart/api/v1/brands")
public class BrandController {
	
	@Autowired private BrandService brandService;
	
	@PostMapping("/auth/add")
	public ResponseEntity<Brand> addBrand(
			@RequestParam("file") MultipartFile file,
			@RequestParam("name") String name
			) {
		
		try {
			Brand brand = brandService.addNew(file, name);
			return ResponseEntity.ok(brand);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/auth/update/{id}")
	public ResponseEntity<String> addBrand(
			@PathVariable("id") Long id,
			@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file
			) {
		
		try {
			brandService.update(id,name,file);
			return ResponseEntity.ok("Brand Updated");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@DeleteMapping("/auth/delete/{id}")
	public ResponseEntity<String> deleteBrand(@PathVariable("id") Long id){
		brandService.deleteById(id);
		return ResponseEntity.ok("Brand is deleted");
	}
	
	
	@GetMapping("/get/all-brands")
	public ResponseEntity<List<Brand>> listOfBrands(){
		return ResponseEntity.ok(brandService.allBrands());
	}
	
	@GetMapping("/get/all-brands-noimage")
	public ResponseEntity<List<BrandResponse>> listOfBrandsIdAndName(){
		return ResponseEntity.ok(brandService.allBrandsIdAndName());
	}
	
	@GetMapping("/get/byid/{id}")
	public ResponseEntity<Brand> getById(@PathVariable("id") Long id) throws IOException{
		Brand brand = brandService.getById(id);
		return ResponseEntity.ok(brand);
	}
	
	@GetMapping("/get/byidRes/{id}")
	public ResponseEntity<BrandResponse> getByIdResponse(@PathVariable("id") Long id){
		Optional<BrandResponse> brand = brandService.getByIdResponse(id);
		return ResponseEntity.ok(brand.get());
	}


	@GetMapping
	public String work() {
		return "brands";
	}
}

/*
 	@PostMapping("/save")
	public Brand save(@RequestBody Brand brand) {
		return brandService.save(brand);
	}
 * 
 * 
 * 
 */
