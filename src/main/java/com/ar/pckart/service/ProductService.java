package com.ar.pckart.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ar.pckart.dto.OrderProduct;
import com.ar.pckart.dto.ProductDTO;
import com.ar.pckart.dto.ProductDetails;
import com.ar.pckart.dto.ProductResponse;
import com.ar.pckart.model.ImageModel;
import com.ar.pckart.model.ImagesData;
import com.ar.pckart.model.Product;
import com.ar.pckart.repo.ProductPageRepo;
import com.ar.pckart.repo.ProductRepo;
import com.ar.pckart.util.ImagesDataUtils;

@Service
public class ProductService {

	@Autowired	private ProductRepo productRepo;
	@Autowired	private ProductPageRepo pageRepo;
	@Autowired	private ImagesDataService imagesDataService;
	
/*============================ SAVE  ======================================================*/
	public ProductDetails save(Product product) {
		Product savedProd = productRepo.save(product);
		return ProductDetails.builder().id(savedProd.getId()).name(savedProd.getName()).price(savedProd.getPrice())
				.quantity(savedProd.getQuantity()).discount(savedProd.getDiscount())
				.category(savedProd.getCategory().getName()).brand(savedProd.getBrand().getName())
				.color(savedProd.getColor()).build();
	}
/*============================ LIST OF PRODUCT RESPONSE ======================================================*/
	public List<ProductResponse> listOfProducts() {
		return productRepo.getAllProductDetailsWithSpecs();
	}
/*============================ BY ID PRODUCT RESPONSE ======================================================*/
	public Product findById(Long id) {
		return productRepo.findById(id).get();
	}
	public ProductResponse productResponseById(Long id) {
		return productRepo.getProductDetailWithSpecsById(id);
	}
/*============================ LIST WITH MAIN IMAGE ======================================================*/
	public List<ProductDTO<byte[], ImageModel>> listOfProductsWithImages() throws IOException {
		List<ProductDTO<byte[], ImageModel>> productsList = new ArrayList<>();
		for (ProductResponse pr : listOfProducts()) {
			ImagesData imageData = imagesDataService.findImageDataByProductId(pr.getProductId()).get();
			byte[] image = ImagesDataUtils.imageFromFolder(imageData.getProductMainImage().getFilePath(),
					imageData.getProductMainImage().getImgName());
			ProductDTO<byte[], ImageModel> pdto = new ProductDTO<>();
			pdto.setProductResponse(pr);
			pdto.setImgdata(image);
			pdto.setImgModel(imageData.getProductMainImage());
			productsList.add(pdto);
		}
		return productsList;
	}
/*============================ BY ID WITH MAIN IMAGE ONLY ======================================================*/
	public ProductDTO<byte[], ImageModel> productDetailWithMainImageById(Long id) throws IOException {
		ProductDTO<byte[], ImageModel> productById = new ProductDTO<>();
		productById.setProductResponse(productResponseById(id));
		ImagesData imageData = imagesDataService.findImageDataByProductId(id).get();

		byte[] image = ImagesDataUtils.imageFromFolder(imageData.getProductMainImage().getFilePath(),
				imageData.getProductMainImage().getImgName());
		productById.setImgdata(image);
		productById.setImgModel(imageData.getProductMainImage());

		return productById;
	}
/*============================ BY ID WITH ALL IMAGES ======================================================*/
	public ProductDTO<List<byte[]>, List<ImageModel>> productDetailsWithImagesById(Long id) throws IOException {
		ProductDTO<List<byte[]>, List<ImageModel>> productById = new ProductDTO<>();
		productById.setProductResponse(productResponseById(id));
		ImagesData imageData = imagesDataService.findImageDataByProductId(id).get();

		List<byte[]> imagesList = new ArrayList<>();
		List<ImageModel> imagesModelList = new ArrayList<>();

		byte[] mainImage = ImagesDataUtils.imageFromFolder(imageData.getProductMainImage().getFilePath(),
				imageData.getProductMainImage().getImgName());

		imagesList.add(mainImage);
		imagesModelList.add(imageData.getProductMainImage());

		Set<ImageModel> productSubImages = imageData.getProductSubImages();
		for (ImageModel im : productSubImages) {
			byte[] image = ImagesDataUtils.imageFromFolder(im.getFilePath(), im.getImgName());
			imagesList.add(image);
			imagesModelList.add(im);
		}

		productById.setImgdata(imagesList);
		productById.setImgModel(imagesModelList);

		return productById;
	}
/*============================ UPDATE ======================================================*/
	public ProductDetails update(Long id, Product product) {
		product.setId(id);
		Product updatedProduct = productRepo.save(product);
		return ProductDetails.builder().id(updatedProduct.getId()).name(updatedProduct.getName())
				.price(updatedProduct.getPrice()).quantity(updatedProduct.getQuantity())
				.discount(updatedProduct.getDiscount()).brand(updatedProduct.getCategory().getName())
				.category(updatedProduct.getBrand().getName()).color(updatedProduct.getColor()).build();
	}
	
	public int getQuantityById(Long id) {
		return productRepo.getQuantityById(id);
	}
	
	public void updateQuantity(Long id,int quantity) {
		int currentQuantity = getQuantityById(id);
		productRepo.updateQuantityById(currentQuantity-quantity, id);
	}
	
	public void updateActiveById(Long id, boolean active) {
		productRepo.updateActiveById(id, active);
	}
/*============================ DELETE ======================================================*/
	public void deleteById(Long id) {
		productRepo.deleteById(id);
	}
	
/*============================ PAGE ======================================================*/
	public Map<String, Object> listAllWithimage(int pageNum, int limit, String sortField, String sortDir, String searchKeyword)
			throws IOException {
		List<ProductDTO<byte[], ImageModel>> productsList = new ArrayList<>();
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, limit, sort); // 10 5 20
		
		Page<ProductResponse> page = null;
		if(searchKeyword != null && !searchKeyword.trim().equals("")) {
			/*---------- PAGE WITH SEARCH KEYWORD FILTER---------*/
			page = pageRepo.getAllProductDetailsWithSpecsFilterByKeyword(pageable, searchKeyword.trim());
		}else {
			/*---------- PAGE WITHOUT SEARCH---------*/
			page = pageRepo.getAllProductDetailsWithSpecs(pageable);
		}

		for (ProductResponse pr : page.getContent()) {
			ImagesData imageData = imagesDataService.findImageDataByProductId(pr.getProductId()).get();
			byte[] image = ImagesDataUtils.imageFromFolder(imageData.getProductMainImage().getFilePath(),
					imageData.getProductMainImage().getImgName());
			ProductDTO<byte[], ImageModel> pdto = new ProductDTO<>();
			pdto.setProductResponse(pr);
			pdto.setImgdata(image);
			pdto.setImgModel(imageData.getProductMainImage());
			productsList.add(pdto);
		}

		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();

		Map<String, Object> map = new HashMap<>();
		map.put("pageNum", pageNum);
		map.put("totalItems", totalItems);
		map.put("totalPages", totalPages);
		map.put("listProducts", productsList);
		map.put("sortField", sortField);
		map.put("sortDir", sortDir);
		map.put("limit", limit);
		
		String reverseSortDir =  sortDir.equals("asc") ? "desc" : "asc" ;
		map.put("reverseSortDir", reverseSortDir);
		
		long startCount = (pageNum - 1) * limit + 1;
		map.put("startCount", startCount);
		
		long endCount = (startCount+limit-1) < totalItems ? (startCount+limit-1) : totalItems;
		map.put("endCount", endCount);

		return map;
	}

	

/*============================ TOP N PRODUCTS WITH MAIN IMAGE BY ADDED DATE=================================*/
	public List<ProductDTO<byte[], ImageModel>> getTopNumsByOrderByAddedAtDesc(Long limit) throws IOException {
		List<ProductDTO<byte[], ImageModel>> productsList = new ArrayList<>();
		for (ProductResponse pr : productRepo.getTopNumsByOrderByAddedAtDesc(limit)) {
			ImagesData imageData = imagesDataService.findImageDataByProductId(pr.getProductId()).get();
			byte[] image = ImagesDataUtils.imageFromFolder(imageData.getProductMainImage().getFilePath(),
					imageData.getProductMainImage().getImgName());
			ProductDTO<byte[], ImageModel> pdto = new ProductDTO<>();
			pdto.setProductResponse(pr);
			pdto.setImgdata(image);
			pdto.setImgModel(imageData.getProductMainImage());
			productsList.add(pdto);
		}
		return productsList;
	}
 /*============================ COUNT ======================================================*/
	public Long getCountOfProducts() {
		return productRepo.count();
	}

	public Long countProductsByCategoryName(String categoryName) {
		return productRepo.countByCategoryName(categoryName);
	}
	
	public Long countByColor(String color) {
		return productRepo.countByColor(color);
	}
/*=======================================PRODUCTS WITH CONDITIONS,FILTER====================================================*/
	
	public List<ProductDTO<byte[], ImageModel>> getProductsByCategoryId(Long categoryId) throws IOException{
		return listOfProdsToDTO(productRepo.getProductsByCategoryId(categoryId));
	}
	public List<ProductDTO<byte[], ImageModel>> getProductsByCategoryName(String categoryName) throws IOException {
		return listOfProdsToDTO(productRepo.getProductsByCategoryName(categoryName));
	}
	public List<ProductDTO<byte[], ImageModel>> getProductsByCategoryNameByFilter(
    		String categoryName,List<String> brandNames,
    		float minPrice,float maxPrice,List<String> colors
    ) throws IOException {
		
		brandNames = brandNames != null ? brandNames : Collections.emptyList();
	    colors = colors != null ? colors : Collections.emptyList();
	    
		return listOfProdsToDTO(productRepo.getProductsByCategoryNameByFilter(
				categoryName,brandNames,minPrice,maxPrice,colors
			));
	}
	
	public List<ProductDTO<byte[], ImageModel>> getProductsByParentCategoryId(Long parentId) throws IOException{
		return listOfProdsToDTO(productRepo.getProductsByParentCategoryId(parentId));
	}
	public List<ProductDTO<byte[], ImageModel>> getProductsByParentCategoryName(String parentName) throws IOException {
		return listOfProdsToDTO(productRepo.getProductsByParentCategoryName(parentName));
	}
	public List<ProductDTO<byte[], ImageModel>> getProductsByParentCategoryNameByFilter(
    		String parentName,List<String> brandNames,
    		float minPrice,float maxPrice,List<String> colors
    ) throws IOException {
		
		brandNames = brandNames != null ? brandNames : Collections.emptyList();
	    colors = colors != null ? colors : Collections.emptyList();
	    
		return listOfProdsToDTO(productRepo.getProductsByParentCategoryNameByFilter(
			parentName,brandNames,minPrice,maxPrice,colors
			));
	}
	
	public List<ProductDTO<byte[], ImageModel>> getProductsByBrandId(Long brandId) throws IOException{
		return listOfProdsToDTO(productRepo.getProductsByBrandId(brandId));
	}
	public List<ProductDTO<byte[], ImageModel>> getProductsByBrandName(String brandName) throws IOException {
		return listOfProdsToDTO(productRepo.getProductsByBrandName(brandName));
	}
	
	public List<ProductDTO<byte[], ImageModel>> getProductsByBrandAndCategoryName(String brandName, String categoryName) throws IOException {
		return listOfProdsToDTO(productRepo.getProductsByBrandAndCategoryName(brandName, categoryName));
	}
	
	public List<ProductDTO<byte[], ImageModel>> getProductsByPriceRange(float start, float end) throws IOException {
		return listOfProdsToDTO(productRepo.getProductsByPriceRange(start, end));
	}
	
	public List<ProductDTO<byte[], ImageModel>> getAllProductsByKeyword(String searchKeyword) throws IOException {
		return listOfProdsToDTO(productRepo.getAllProductsByKeyword(searchKeyword));
	}
	
	public List<ProductResponse> getAllProductsRepsonsesByKeywordAndLimit(String searchKeyword, Long limit) throws IOException {
		return productRepo.getAllProductsByKeywordAndLimit(searchKeyword,limit);
	}
	
	public List<ProductDTO<byte[], ImageModel>> getAllProductsByKeywordAndLimit(String searchKeyword, Long limit) throws IOException {
		return listOfProdsToDTO(getAllProductsRepsonsesByKeywordAndLimit(searchKeyword, limit));
	}

	public List<ProductDTO<byte[], ImageModel>> getProductsWithQuantitySort(Long limit, String sortString) throws IOException {
		return sortString.equals("asc") ? listOfProdsToDTO(productRepo.getProductsByQuantitySortLess(limit)) : 
			listOfProdsToDTO(productRepo.getProductsByQuantitySortMore(limit));
	}

	
/*=================COMMON PRODUCT RESPONSE TO DTO==================*/
	public List<ProductDTO<byte[], ImageModel>> listOfProdsToDTO(List<ProductResponse> productResponseList) throws IOException {
		List<ProductDTO<byte[], ImageModel>> productsList = new ArrayList<>();
		for (ProductResponse pr : productResponseList) {
			ImagesData imageData = imagesDataService.findImageDataByProductId(pr.getProductId()).get();
			byte[] image = ImagesDataUtils.imageFromFolder(imageData.getProductMainImage().getFilePath(),
					imageData.getProductMainImage().getImgName());
			ProductDTO<byte[], ImageModel> pdto = new ProductDTO<>();
			pdto.setProductResponse(pr);
			pdto.setImgdata(image);
			pdto.setImgModel(imageData.getProductMainImage());
			productsList.add(pdto);
		}
		return productsList;
	}
	
/*=================PRODUCTS GRANDTOTAL==================*/
	public Double getProductsGrandTotal(List<ProductDetails> products) {
		Double grandTotal = 0.0;
		for(ProductDetails prod: products) {
			Double total = productResponseById(prod.getId()).getProductPrice() * prod.getQuantity();
			grandTotal += total;
		}

		return grandTotal;
	}
	/*=================PRODUCTS QUANTITY CHANGE BYID==================*/	
	public String updateProductQuantiyById(Long id, int quantity) {
		productRepo.updateQuantityById(quantity, id);
		return "Product Quantity changed successfully";
	}
/*=================PRODUCTS QUANTITY CHANGE==================*/	
	public String updateProductsQuantiy(List<OrderProduct> orderProducts, String type) { //ADD DELETE
		for(OrderProduct orderProduct: orderProducts) {
			int existingQuantity = getQuantityById(orderProduct.getProductId());
			int updateQty = type.equals("ADD") ? existingQuantity+orderProduct.getProductQuantity() : existingQuantity-orderProduct.getProductQuantity(); 
//			productRepo.updateQuantityById(existingQuantity-orderProduct.getProductQuantity(), orderProduct.getProductId());
			productRepo.updateQuantityById(updateQty, orderProduct.getProductId());
		}
		return "Products Quantity changed successfully";
	}
	
}

