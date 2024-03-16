package com.ar.pckart.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ar.pckart.dto.CategoryResponse;
import com.ar.pckart.model.Category;
import com.ar.pckart.repo.CategoryRepo;
import com.ar.pckart.util.ImageUtils;


@Service
public class CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	
	public Category addCategoryWithImage(Category category, MultipartFile file) throws IOException {
		category.setImage(ImageUtils.compress(file.getBytes()));
		category.setImageName(file.getOriginalFilename());
		category.setImageType(file.getContentType());
		return categoryRepo.save(category);
	}

	public void updateCategoryWithImage(Long id, Category category, MultipartFile file) throws IOException {
		//category.setImage(ImageUtils.compress(file.getBytes()));
		categoryRepo.updateCategory(id, category.getName(),
				ImageUtils.compress(file.getBytes()),
				file.getOriginalFilename(),
				file.getContentType(), category.getParent());
	}

	public void deleteCategory(Long id) { 
		categoryRepo.deleteById(id); 
	}

	public Category getById(Long id) throws IOException {
		Category category = categoryRepo.findById(id).get();
		category.setImage(ImageUtils.decompress(category.getImage()));
		return category;
	}

	public List<Category> allCategories() {
		//List<Category> categories = categoryRepo.findAll();
		List<Category> categories = categoryRepo.findAllByOrderByAddedatDesc();
		categories.forEach(category -> {
				category.setImage(ImageUtils.decompress(category.getImage()));
		});
		return categories;
	}

	public List<CategoryResponse> allCategoriesIdAndName() {
		return categoryRepo.allCategoriesIdAndName();
	}
	
	public List<CategoryResponse> allSubcategoriesCategoriesByParentName(String parentCategory) {
		return categoryRepo.allSubCategoriesCategoriesIdAndName(parentCategory);
	}

	public Optional<CategoryResponse> getByIdCategoryResponse(Long id) {
		return categoryRepo.findByIdCategoryResponse(id);
	}

	public List<Category> allParentCategories(){
		return categoryRepo.allParentCategories();
	}
	
	public List<Category> allCategoriesByParentName(String parentCategory){
		List<Category> categories =  categoryRepo.allCategoriesByParentName(parentCategory);
		categories.forEach(category -> {
			category.setImage(ImageUtils.decompress(category.getImage()));
		});
		return categories;
	}
	
	public List<Category> allSubcategoriesCategories(){
		List<Category> categories =  categoryRepo.allSubcategoriesCategories();
		categories.forEach(category -> {
			category.setImage(ImageUtils.decompress(category.getImage()));
		});
		return categories;
	}
	
	public String getParentCategoryNameByCategory(String category) {
		return categoryRepo.getParentCategoryNameByCategory(category);
	}


}
/*
 * public Category save(Category category) { return categoryRepo.save(category);
 * }
 * 
 * 
 * public void updateCategory(Long id, Category category) {
 * categoryRepo.updateCategory(id, category.getName(), category.getParent()); }
 */
