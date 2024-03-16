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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.pckart.dto.CategoryResponse;
import com.ar.pckart.model.Category;
import com.ar.pckart.service.CategoryService;

@RestController
@RequestMapping("/pckart/api/v1/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/auth/add")
	public ResponseEntity<Category> addCategory(
			@RequestPart("category") Category category,
			@RequestParam("file") MultipartFile file
			) {
		try {
			Category categorySaved = categoryService.addCategoryWithImage(category, file);
			return ResponseEntity.ok(categorySaved);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/get/all-categories")
	public ResponseEntity<List<Category>> listOfCategories(){
		return ResponseEntity.ok(categoryService.allCategories());
	}
	
	@GetMapping("/get/all-categories-noimage")
	public ResponseEntity<List<CategoryResponse>> allCategoriesIdAndName(){
		return ResponseEntity.ok(categoryService.allCategoriesIdAndName());
	}
	
	@GetMapping("/get/all-subcategories-noimage/by-parent/{parentCategory}")
	public ResponseEntity<List<CategoryResponse>> allCategoriesResponsesByParentName(
			@PathVariable("parentCategory") String parentCategory){
		List<CategoryResponse> allSubcategoriesCategoriesByParentName = categoryService.allSubcategoriesCategoriesByParentName(parentCategory);
		System.err.println(allSubcategoriesCategoriesByParentName);
		return ResponseEntity.ok(allSubcategoriesCategoriesByParentName);
	}
	
	@GetMapping("/get/byid/{id}")
	public ResponseEntity<Category> getById(@PathVariable("id") Long id) throws IOException{
		Category category = categoryService.getById(id);
		return ResponseEntity.ok(category);
	}
	
	@GetMapping("/get/byidRes/{id}")
	public ResponseEntity<CategoryResponse> getByIdResponse(@PathVariable("id") Long id){
		Optional<CategoryResponse> category = categoryService.getByIdCategoryResponse(id);
		return ResponseEntity.ok(category.get());
	}
	
	@PutMapping("/auth/update/{id}")
	public ResponseEntity<String> updateCategory(
			@PathVariable("id") Long id,
			@RequestPart("category") Category category,
			@RequestParam("file") MultipartFile file){
		
		try {
			categoryService.updateCategoryWithImage(id, category, file);
			return ResponseEntity.ok("Category is updated");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DeleteMapping("/auth/delete/{id}")
	public ResponseEntity<String> deleteCategory(
			@PathVariable("id") Long id){
		
		categoryService.deleteCategory(id);
		return ResponseEntity.ok("Category is deleted");
	}
	
	@GetMapping("/get/all-parent-categories")
	public ResponseEntity<List<Category>> allParentCategories(){
		return ResponseEntity.ok(categoryService.allParentCategories());
	}
	
	@GetMapping("/get/all-sub-categories")
	public ResponseEntity<List<Category>> allSubcategoriesCategories(){
		return ResponseEntity.ok(categoryService.allSubcategoriesCategories());
	}
	
	@GetMapping("/get/all-categories/by-parent/{parentCategory}")
	public ResponseEntity<List<Category>> allCategoriesByParentName(
			@PathVariable("parentCategory") String parentCategory){
		return ResponseEntity.ok(categoryService.allCategoriesByParentName(parentCategory));
	}
	
	@GetMapping("/get/parent-category/bycategory/{category}")
	public ResponseEntity<String> getParentCategoryNameByCategory(@PathVariable("category") String category) {
		return ResponseEntity.ok(categoryService.getParentCategoryNameByCategory(category));
	}
	
	@GetMapping
	public String work() {
		return "Categories";
	}
}

/*
 * 
 	@PostMapping("/save")
	public Category save(@RequestBody Category category) {
		return categoryService.save(category);
	}
	
 * 
 	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateCategory(
			@PathVariable("id") Long id,
			@RequestBody Category category){
		
		categoryService.updateCategory(id, category);
		return ResponseEntity.ok("Category is updated");
	} 
 
 */
