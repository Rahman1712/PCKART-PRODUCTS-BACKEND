package com.ar.pckart.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.pckart.dto.OrderProduct;
import com.ar.pckart.dto.ProductDTO;
import com.ar.pckart.dto.ProductDetails;
import com.ar.pckart.dto.ProductResponse;
import com.ar.pckart.model.ImageModel;
import com.ar.pckart.model.Product;
import com.ar.pckart.service.ImagesDataService;
import com.ar.pckart.service.ProductService;

@RestController
@RequestMapping("/pckart/api/v1/products")
public class ProductController {
	
	@Autowired private ProductService productService;
	@Autowired private ImagesDataService imagesService;

	@PostMapping("/auth/add-product-imgs")
	public ProductDetails saveWithMultipleImage(
			@RequestParam(name = "file") MultipartFile file,
			@RequestParam(name = "files") MultipartFile[] files,
			@RequestPart Product product
			) throws IOException {
		
		ProductDetails savedProduct = productService.save(product);
		imagesService.saveImages(file, files, savedProduct.getId());
		
		return savedProduct; 
	}

	@PutMapping("/auth/update-product/{id}")
	public ProductDetails updateImageInfo(
			@PathVariable("id") Long id,
			@RequestParam(name = "file") MultipartFile file,
			@RequestParam(name = "files") MultipartFile[] files,
			@RequestPart Product product
			) throws IOException {
		
		ProductDetails updatedProduct = productService.update(id,product);
		imagesService.updateImages(file, files, updatedProduct.getId());
		
		return updatedProduct;
	}
	
	@PutMapping("/auth/update-active/byid/{id}/{active}")
	public ResponseEntity<String> updateActiveById(@PathVariable("id")Long id,
			@PathVariable("active")boolean active){
		productService.updateActiveById(id, active);
		return ResponseEntity.ok("product active updated");
	}
	
	@DeleteMapping("/auth/delete-product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
		productService.deleteById(id);
		imagesService.deleteImageDataAndFolder(id);
		return ResponseEntity.ok("Product deleted");
	}
	
	@GetMapping("/get/all-products")
	public ResponseEntity<List<ProductResponse>> allProducts(){
		return ResponseEntity.ok(productService.listOfProducts());
	}
	
	@GetMapping("/get/all-product-imgs")
	public ResponseEntity<?> listOfProducts() throws IOException {
		List<ProductDTO<byte[], ImageModel>> listOfProds = productService.listOfProductsWithImages();
		return ResponseEntity.status(HttpStatus.OK).body(listOfProds);	
	}
	
	@GetMapping("/get/product/{id}")
	public ResponseEntity<?> getProductDetailsMainImageById(@PathVariable Long id) throws IOException {
		ProductDTO<byte[], ImageModel> productInfo = productService.productDetailWithMainImageById(id);
		return ResponseEntity.status(HttpStatus.OK).body(productInfo);	
	}
	
	@GetMapping("/get/product-imgs/{id}")
	public ResponseEntity<?> getProductDetailsImagesById(@PathVariable Long id) throws IOException {
		ProductDTO<List<byte[]>, List<ImageModel>> productInfo = productService.productDetailsWithImagesById(id);
		return ResponseEntity.status(HttpStatus.OK).body(productInfo);	
	}
	
	@GetMapping("/get/product-res/{id}")
	public ResponseEntity<ProductResponse> getProductResponseById(
			@PathVariable("id") Long id){
		return ResponseEntity.ok(productService.productResponseById(id));
	}
	
	@GetMapping("/get/page-imgs/{pageNum}" )
	public Map<String, Object> listAllWithimage(
			@PathVariable("pageNum") int pageNum , 
			@Param("limit") int limit,
			@Param("sortField") String sortField , 
			@Param("sortDir") String sortDir ,
			@Param("searchKeyword") String searchKeyword) throws IOException {
		
		Map<String, Object> map = productService.listAllWithimage(pageNum,limit, sortField,sortDir,searchKeyword );
		
		
		return map;
	}
	
	@GetMapping("/get/recent-products/limit/{limit}")
	public ResponseEntity<?> getTopProductsByAddedAt(@PathVariable("limit")Long limit) throws IOException{
		var produtsList = productService.getTopNumsByOrderByAddedAtDesc(limit);
		return ResponseEntity.status(HttpStatus.OK).body(produtsList);
	}
	
	@GetMapping("/get/product-count/by-category-name/{categoryName}" )
	public ResponseEntity<String> countProductsByCategoryName(@PathVariable("categoryName") String categoryName) {
		Long count = productService.countProductsByCategoryName(categoryName);
        return ResponseEntity.ok(String.valueOf(count));
    }

	@GetMapping("/get/product-count")
	public ResponseEntity<?> countOfProducts(){
		return ResponseEntity.ok(String.valueOf(productService.getCountOfProducts()));
	}
	
	@GetMapping("/get/product-count/by-color/{color}")
	public ResponseEntity<?> countByColor(@PathVariable("color")String color){
		return ResponseEntity.ok(String.valueOf(productService.countByColor(color)));
	}
	
	@PutMapping("/auth/update-quantity/{id}")
	public ResponseEntity<String> updateProductQuantityById(@RequestParam("quantity") int quantity,
			@PathVariable("id") Long id) {
		productService.updateQuantity(id,quantity);
		return ResponseEntity.ok("Product quantity updated"); 
	}
	
	@GetMapping("/get/quantity/{id}")
	public ResponseEntity<?> quantityOfProduct(@PathVariable("id")Long id){
		return ResponseEntity.ok(String.valueOf(productService.getQuantityById(id)));
	}
	
	@GetMapping
	public String work() {
		return "products";
	}
	@GetMapping("/test")
	public String test() {
		return "products test";
	}

	@GetMapping("/get/all/bycategory/byid/{categoryId}")
	public ResponseEntity<?> getProductsByCategoryId(@PathVariable("categoryId")Long categoryId) throws IOException{
		return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId));
	}
	@GetMapping("/get/all/bycategory/byname/{categoryName}")
	public ResponseEntity<?> getProductsByCategoryName(@PathVariable("categoryName")String categoryName) throws IOException {
		return ResponseEntity.ok(productService.getProductsByCategoryName(categoryName));
	}
	@GetMapping("/get/all/bycategory/filter/byname/{categoryName}")
	public ResponseEntity<?> getProductsByCategoryNameByFilter(
			@PathVariable("categoryName")String categoryName,
			@RequestParam(required = false) List<String> brandNames,
            @RequestParam(required = false) float minPrice,
            @RequestParam(required = false) float maxPrice,
            @RequestParam(required = false) List<String> colors
			) throws IOException {
		return ResponseEntity.ok(productService.getProductsByCategoryNameByFilter
				(categoryName, brandNames, minPrice, maxPrice, colors));
	}
	
	@GetMapping("/get/all/bycategory/parent/byid/{parentId}")
	public ResponseEntity<?> getProductsByParentCategoryId(@PathVariable("parentId")Long parentId) throws IOException{
		return ResponseEntity.ok(productService.getProductsByParentCategoryId(parentId));
	}
	@GetMapping("/get/all/bycategory/parent/byname/{parentName}")
	public ResponseEntity<?> getProductsByParentCategoryName(@PathVariable("parentName")String parentName) throws IOException {
		return ResponseEntity.ok(productService.getProductsByParentCategoryName(parentName));
	}
	@GetMapping("/get/all/bycategory/filter/parent/byname/{parentName}")
	public ResponseEntity<?> getProductsByParentCategoryNameByFilter(
			@PathVariable("parentName")String parentName,
			@RequestParam(required = false) List<String> brandNames,
            @RequestParam(required = false) float minPrice,
            @RequestParam(required = false) float maxPrice,
            @RequestParam(required = false) List<String> colors
			) throws IOException {
		return ResponseEntity.ok(productService.getProductsByParentCategoryNameByFilter
				(parentName, brandNames, minPrice, maxPrice, colors));
	}
	
	@GetMapping("/get/all/bybrand/byid/{brandId}")
	public ResponseEntity<?> getProductsByBrandId(@PathVariable("brandId")Long brandId) throws IOException{
		return ResponseEntity.ok(productService.getProductsByBrandId(brandId));
	}
	@GetMapping("/get/all/bybrand/byname/{brandName}")
	public ResponseEntity<?> getProductsByBrandName(@PathVariable("brandName")String brandName) throws IOException {
		return ResponseEntity.ok(productService.getProductsByBrandName(brandName));
	}
	
	@GetMapping("/get/all/byBrandAndCategory")
	public ResponseEntity<?> getProductsByBrandAndCategoryName(@RequestParam("brandName")String brandName,@RequestParam("categoryName")String categoryName) throws IOException {
		return ResponseEntity.ok(productService.getProductsByBrandAndCategoryName(brandName, categoryName));
	}
	
	@GetMapping("/get/all/byPriceLimit")
	public ResponseEntity<?> getProductsByPriceRange(@RequestParam("start")float start, @RequestParam("end")float end) throws IOException {
		return ResponseEntity.ok(productService.getProductsByPriceRange(start, end));
	}
	
	@GetMapping("/get/all/bySearch/{searchKeyword}")
	public ResponseEntity<?> getAllProductsByKeyword(@PathVariable("searchKeyword")String searchKeyword) throws IOException {
		return ResponseEntity.ok(productService.getAllProductsByKeyword(searchKeyword));
	}
	
	@GetMapping("/get/all/prod-res/bySearch_and_Limit")
	public ResponseEntity<?> getAllProductsResponsesByKeywordAndLimit(
			@RequestParam("searchKeyword")String searchKeyword, @RequestParam("limit")Long limit)
			throws IOException {
		return ResponseEntity.ok(productService.getAllProductsRepsonsesByKeywordAndLimit(searchKeyword, limit));
	}
	
	@GetMapping("/get/all/bySearch_and_Limit")
	public ResponseEntity<?> getAllProductsByKeywordAndLimit(
			@RequestParam("searchKeyword")String searchKeyword, @RequestParam("limit")Long limit)
					throws IOException {
		return ResponseEntity.ok(productService.getAllProductsByKeywordAndLimit(searchKeyword, limit));
	}

	@PostMapping("/get/total-of-products")
	public ResponseEntity<Double> getProductsGrandTotal(
			@RequestBody List<ProductDetails> products
			){
		return ResponseEntity.ok(productService.getProductsGrandTotal(products));
	}
	
	@PutMapping("/auth/change-quantity/{productId}")
	public ResponseEntity<String> updateProductQuantiyById(@PathVariable("productId") Long productId, @RequestParam("quantity") int quantity){
		return ResponseEntity.ok(productService.updateProductQuantiyById(productId, quantity));
	}
	
	@PutMapping("/auth/change-products-quantity")
	public ResponseEntity<String> updateProductsQuantiy(@RequestBody List<OrderProduct> orderProducts, @RequestParam("type") String type){
		return ResponseEntity.ok(productService.updateProductsQuantiy(orderProducts, type));
	}
	
	@GetMapping("/getprod/{id}")
	public ResponseEntity<?> getProd(@PathVariable Long id) throws IOException {
		return ResponseEntity.status(HttpStatus.OK).body(productService.findById(id));	
	}
	
	@GetMapping("/getprod/quantitySort/{limit}")
	public ResponseEntity<?> getProductsWithQuantitySort(@PathVariable("limit") Long limit, 
			@RequestParam("sort") String sort) throws IOException{
		return ResponseEntity.ok(productService.getProductsWithQuantitySort(limit, sort));
	}
}
