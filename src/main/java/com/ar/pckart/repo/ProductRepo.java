package com.ar.pckart.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.pckart.dto.ProductResponse;
import com.ar.pckart.model.Product;

import jakarta.transaction.Transactional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>{
	
	@Query(value = "CREATE FULLTEXT INDEX idx_product_name ON products(product_name)", nativeQuery = true)
	void createProductNameFullTextIndex();
	@Query(value = "CREATE FULLTEXT INDEX idx_product_description ON products(description)", nativeQuery = true)
	void createProductDescriptionFullTextIndex();

	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at, p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "ORDER BY p.added_at DESC")
    List<ProductResponse> getAllProductsDetails();

	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at, p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.id = ?1 ")
	ProductResponse getProductDetailById(Long id);
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at, p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.name = ?1 ")
	ProductResponse getProductDetailByName(String name);
	
	@Query(nativeQuery = true, value = "SELECT specs_key, specs FROM product_specs WHERE spec_id = :id")
	List<Map<String, String>> getProductSpecsById(@Param("id") Long id);

	default List<ProductResponse> getAllProductDetailsWithSpecs() {
	    List<ProductResponse> productResponses = getAllProductsDetails();

	    for (ProductResponse response : productResponses) {
	        Long productId = response.getProductId();
	        List<Map<String, String>> specs = getProductSpecsById(productId);
	        response.setProductSpecs(specs);
	    }

	    return productResponses;
	}
	
	default ProductResponse getProductDetailWithSpecsById(Long id) {
	    ProductResponse response = getProductDetailById(id);
	    response.setProductSpecs(getProductSpecsById(response.getProductId()));

	    return response;
	}
	
	@Modifying
	@Transactional
	@Query("update Product u set u.name = :name where u.id = :id")
	void updateName(@Param(value = "id") Long id, @Param(value = "name") String name);
	
	@Modifying
	@Transactional
	@Query("update Product u set u.active = :active where u.id = :id")
	void updateActiveById(@Param(value = "id") Long id, @Param(value = "active") boolean active);
	
	@Query("SELECT p.quantity FROM Product p WHERE p.id = :id")
	public int getQuantityById(@Param("id") Long id);
	@Query("SELECT p.quantity FROM Product p WHERE p.name = :name")
	public int getQuantityByName(@Param("name") String name);
	
	@Query("SELECT DISTINCT(p.brand.name) from Product p")
	List<String> listOfDistinctBrands();
	
	@Query("SELECT p.brand.name,p.brand.id from Product p GROUP BY p.brand.name")
	List<String> listOfDistinctBrandsImgs();
	
	@Query("SELECT count(*) from Product p WHERE p.category.name = :categoryName")
	Long countByCategoryName(String categoryName);
	
	@Query("SELECT count(*) from Product p WHERE p.color = :color")
	Long countByColor(String color);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "ORDER BY p.added_at DESC LIMIT ?1")
    List<ProductResponse> getTopNumsByOrderByAddedAtDesc(Long limit);
	
	@Modifying
	@Transactional
	@Query("update Product u set u.quantity = :quantity where u.id = :id")
	public Integer updateQuantityById(@Param("quantity") int quantity,@Param("id") Long id);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.category.id = ?1 ")
    List<ProductResponse> getProductsByCategoryId(Long categoryId);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.category.name = ?1 ")
	List<ProductResponse> getProductsByCategoryName(String categoryName);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.category.parent.id = ?1 ")
    List<ProductResponse> getProductsByParentCategoryId(Long parentId);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.category.parent.name = ?1 ")
	List<ProductResponse> getProductsByParentCategoryName(String parentName);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.brand.id = ?1 ")
    List<ProductResponse> getProductsByBrandId(Long brandId);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.brand.name = ?1 ")
	List<ProductResponse> getProductsByBrandName(String brandName);
	

	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE "
			+ "p.brand.name = ?1 "
			+ "AND p.category.name = ?2 ")
	List<ProductResponse> getProductsByBrandAndCategoryName(String brandName, String categoryName);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE "
			+ "p.price BETWEEN ?1 AND ?2")
	List<ProductResponse> getProductsByPriceRange(float start, float end);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount, "
			+ "p.color, p.description, "
			+ "b.id, b.name,"
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE "
			+ "CONCAT(p.id, ' ',p.name, ' ',p.description, ' ',p.color, ' ',b.name, ' ', c.name) "
			+ "LIKE %:searchKeyword%")
	List<ProductResponse> getAllProductsByKeyword(@Param("searchKeyword") String searchKeyword);

	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount, "
			+ "p.color, p.description, "
			+ "b.id, b.name,"
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE "
			//+ "CONCAT(p.id, ' ',p.name, ' ',p.description, ' ',p.color, ' ',b.name, ' ', c.name) "
			+ "CONCAT(p.name, ' ',p.description, ' ',p.color, ' ',b.name, ' ', c.name) "
			+ "LIKE %:searchKeyword% "
			+ "ORDER BY p.name ASC LIMIT :limit")  
	List<ProductResponse> getAllProductsByKeywordAndLimit(
			@Param("searchKeyword") String searchKeyword,
			@Param("limit") Long limit); 
	
	
	/*
    @Query(value = "SELECT "
			+ "p.product_id, p.product_name, "
			+ "p.price, p.quantity, p.discount, "
	        + "p.color, p.description, "
	        + "b.brand_id, b.brand_name, c.category_id, c.category_name, "
	        + "p.added_at, p.active "
	        + "FROM products p "
	        + "JOIN brands b ON p.brand_id = b.brand_id "
	        + "JOIN categories c ON p.category_id = c.category_id "
            + "WHERE MATCH(p.product_name) "
            + "AGAINST(:searchKeyword IN BOOLEAN MODE) "
            + "ORDER BY p.product_name ASC LIMIT :limit", nativeQuery = true)
    List<Product> getAllProductsByKeywordAndLimit(@Param("searchKeyword") String searchKeyword, @Param("limit") Long limit);
	*/

	
/*================================CATEGORY FILTER======================================*/
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
	        + "p.id, p.name, "
	        + "p.price, p.quantity, p.discount,  "
	        + "p.color, p.description, "
	        + "b.id, b.name, "
	        + "c.id, c.name, "
	        + "p.added_at, p.active) "
	        + "FROM Product p "
	        + "JOIN p.brand b "
	        + "JOIN p.category c "
	        + "WHERE p.category.parent.name = :parentName "
	        + "AND p.price >= :minPrice "
	        + "AND p.price <= :maxPrice ")
	List<ProductResponse> getProductsByParentCategoryNameByFilterOnlyPrice(
	        @Param("parentName") String parentName,
	        @Param("minPrice") Float minPrice,
	        @Param("maxPrice") Float maxPrice
	);
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
	        + "p.id, p.name, "
	        + "p.price, p.quantity, p.discount,  "
	        + "p.color, p.description, "
	        + "b.id, b.name, "
	        + "c.id, c.name, "
	        + "p.added_at, p.active) "
	        + "FROM Product p "
	        + "JOIN p.brand b "
	        + "JOIN p.category c "
	        + "WHERE p.category.parent.name = :parentName "
	        + "AND p.brand.name IN :brandNames "
	        + "AND p.price >= :minPrice "
	        + "AND p.price <= :maxPrice "
	        + "AND p.color IN :colors")
	List<ProductResponse> getProductsByParentCategoryNameByFilterAll(
	        @Param("parentName") String parentName,
	        @Param("brandNames") List<String> brandNames,
	        @Param("minPrice") Float minPrice,
	        @Param("maxPrice") Float maxPrice,
	        @Param("colors") List<String> colors
	);
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
	        + "p.id, p.name, "
	        + "p.price, p.quantity, p.discount,  "
	        + "p.color, p.description, "
	        + "b.id, b.name, "
	        + "c.id, c.name, "
	        + "p.added_at, p.active) "
	        + "FROM Product p "
	        + "JOIN p.brand b "
	        + "JOIN p.category c "
	        + "WHERE p.category.parent.name = :parentName "
	        + "AND p.brand.name IN :brandNames "
	        + "AND p.price >= :minPrice " 
	        + "AND p.price <= :maxPrice ")
	List<ProductResponse> getProductsByParentCategoryNameByFilterBrandOnly(
	        @Param("parentName") String parentName,
	        @Param("brandNames") List<String> brandNames,
	        @Param("minPrice") Float minPrice,
	        @Param("maxPrice") Float maxPrice
	);
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at, p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.category.parent.name = :parentName "
			+ "AND p.price >= :minPrice "
			+ "AND p.price <= :maxPrice "
			+ "AND p.color IN :colors")
	List<ProductResponse> getProductsByParentCategoryNameByFilterColorsOnly(
			@Param("parentName") String parentName,
			@Param("minPrice") Float minPrice,
			@Param("maxPrice") Float maxPrice,
			@Param("colors") List<String> colors
			);
	default List<ProductResponse> getProductsByParentCategoryNameByFilter(
			String parentName,
			List<String> brandNames,
			Float minPrice,
			Float maxPrice,
			List<String> colors
			){
		if(brandNames.isEmpty() && colors.isEmpty()) {
			return getProductsByParentCategoryNameByFilterOnlyPrice(parentName,  minPrice, maxPrice);
		}
		else if(brandNames.isEmpty()) {
			return getProductsByParentCategoryNameByFilterColorsOnly(parentName, minPrice, maxPrice, colors);
		}
		else if(colors.isEmpty()) {
			return getProductsByParentCategoryNameByFilterBrandOnly(parentName, brandNames, minPrice, maxPrice);
		}
		else {
			return getProductsByParentCategoryNameByFilterAll(parentName, brandNames, minPrice, maxPrice, colors);
		}
	}
/*============================================================================================*/
/*================================SUB CATEGORY FILTER======================================*/
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
	        + "p.id, p.name, "
	        + "p.price, p.quantity, p.discount,  "
	        + "p.color, p.description, "
	        + "b.id, b.name, "
	        + "c.id, c.name, "
	        + "p.added_at, p.active) "
	        + "FROM Product p "
	        + "JOIN p.brand b "
	        + "JOIN p.category c "
	        + "WHERE p.category.name = :categoryName "
	        + "AND p.price >= :minPrice "
	        + "AND p.price <= :maxPrice ")
	List<ProductResponse> getProductsByCategoryNameByFilterOnlyPrice(
	        @Param("categoryName") String categoryName,
	        @Param("minPrice") Float minPrice,
	        @Param("maxPrice") Float maxPrice
	);
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
	        + "p.id, p.name, "
	        + "p.price, p.quantity, p.discount,  "
	        + "p.color, p.description, "
	        + "b.id, b.name, "
	        + "c.id, c.name, "
	        + "p.added_at, p.active) "
	        + "FROM Product p "
	        + "JOIN p.brand b "
	        + "JOIN p.category c "
	        + "WHERE p.category.name = :categoryName "
	        + "AND p.brand.name IN :brandNames "
	        + "AND p.price >= :minPrice "
	        + "AND p.price <= :maxPrice "
	        + "AND p.color IN :colors")
	List<ProductResponse> getProductsByCategoryNameByFilterAll(
	        @Param("categoryName") String categoryName,
	        @Param("brandNames") List<String> brandNames,
	        @Param("minPrice") Float minPrice,
	        @Param("maxPrice") Float maxPrice,
	        @Param("colors") List<String> colors
	);
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
	        + "p.id, p.name, "
	        + "p.price, p.quantity, p.discount,  "
	        + "p.color, p.description, "
	        + "b.id, b.name, "
	        + "c.id, c.name, "
	        + "p.added_at, p.active) "
	        + "FROM Product p "
	        + "JOIN p.brand b "
	        + "JOIN p.category c "
	        + "WHERE p.category.name = :categoryName "
	        + "AND p.brand.name IN :brandNames "
	        + "AND p.price >= :minPrice " 
	        + "AND p.price <= :maxPrice ")
	List<ProductResponse> getProductsByCategoryNameByFilterBrandOnly(
	        @Param("categoryName") String categoryName,
	        @Param("brandNames") List<String> brandNames,
	        @Param("minPrice") Float minPrice,
	        @Param("maxPrice") Float maxPrice
	);
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at, p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.category.name = :categoryName "
			+ "AND p.price >= :minPrice "
			+ "AND p.price <= :maxPrice "
			+ "AND p.color IN :colors")
	List<ProductResponse> getProductsByCategoryNameByFilterColorsOnly(
			@Param("categoryName") String categoryName,
			@Param("minPrice") Float minPrice,
			@Param("maxPrice") Float maxPrice,
			@Param("colors") List<String> colors
			);
	default List<ProductResponse> getProductsByCategoryNameByFilter(
			String categoryName,
			List<String> brandNames,
			Float minPrice,
			Float maxPrice,
			List<String> colors
			){
		if(brandNames.isEmpty() && colors.isEmpty()) {
			return getProductsByCategoryNameByFilterOnlyPrice(categoryName,  minPrice, maxPrice);
		}
		else if(brandNames.isEmpty()) {
			return getProductsByCategoryNameByFilterColorsOnly(categoryName, minPrice, maxPrice, colors);
		}
		else if(colors.isEmpty()) {
			return getProductsByCategoryNameByFilterBrandOnly(categoryName, brandNames, minPrice, maxPrice);
		}
		else {
			return getProductsByCategoryNameByFilterAll(categoryName, brandNames, minPrice, maxPrice, colors);
		}
	}
/*===============================LESS MORE QUANTITY LIMIT,SORT =======================================*/
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount, "
			+ "p.color, p.description, "
			+ "b.id, b.name,"
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "ORDER BY p.quantity DESC LIMIT :limit")  
	List<ProductResponse> getProductsByQuantitySortMore(
			@Param("limit") Long limit);
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount, "
			+ "p.color, p.description, "
			+ "b.id, b.name,"
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "ORDER BY p.quantity ASC LIMIT :limit")  
	List<ProductResponse> getProductsByQuantitySortLess(
			@Param("limit") Long limit);
	/*======================================================================*/
	
} 







/*
 @Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount,  "
			+ "p.color, p.description, "
			+ "b.id, b.name, "
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c "
			+ "WHERE p.category.parent.name = :parentName " 
	        + "AND (:brandNames IS EMPTY OR p.brand.name IN :brandNames) " 
	        + "AND (:minPrice IS NULL OR p.price >= :minPrice) " 
	        + "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " 
	        + "AND (:colors IS EMPTY OR p.color IN :colors)")
	    List<ProductResponse> getProductsByParentCategoryNameByFilter(
	    		@Param("parentName") String parentName,
	            @Param("brandNames") List<String> brandNames,
	            @Param("minPrice") float minPrice,
	            @Param("maxPrice") float maxPrice,
	            @Param("colors") List<String> colors 
	    );
 */
