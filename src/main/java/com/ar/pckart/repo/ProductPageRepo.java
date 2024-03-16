package com.ar.pckart.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.pckart.dto.ProductResponse;
import com.ar.pckart.model.Product;

@Repository
public interface ProductPageRepo extends PagingAndSortingRepository<Product, Long>{
	
	@Query("SELECT new com.ar.pckart.dto.ProductResponse("
			+ "p.id, p.name, "
			+ "p.price, p.quantity, p.discount, "
			+ "p.color, p.description, "
			+ "b.id, b.name,"
			+ "c.id, c.name, "
			+ "p.added_at,p.active) "
			+ "FROM Product p "
			+ "JOIN p.brand b "
			+ "JOIN p.category c")
	public Page<ProductResponse> getAllProducts(Pageable pageable);


	@Query(nativeQuery = true, value = "SELECT specs_key, specs FROM product_specs WHERE spec_id = :id")
	List<Map<String, String>> getProductSpecsById(@Param("id") Long id);

	default Page<ProductResponse> getAllProductDetailsWithSpecs(Pageable pageable) {
		Page<ProductResponse> productResponses = getAllProducts(pageable);

	    for (ProductResponse response : productResponses) {
	        Long productId = response.getProductId();
	        List<Map<String, String>> specs = getProductSpecsById(productId);
	        response.setProductSpecs(specs);
	    }

	    return productResponses;
	}
	
	@EntityGraph(attributePaths = "roles")
	@Query("FROM Product product")
	Page<Product> findAllWithRoles(Pageable pageable);

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
			+ "CONCAT(p.id, ' ',p.name, ' ',b.name, ' ', c.name, ' ', p.color) "
			+ "LIKE %:searchKeyword%")  //WHERE p.name LIKE %:searchKeyword%
	public Page<ProductResponse> getAllProductsByKeyword(
			Pageable pageable,
			@Param("searchKeyword") String searchKeyword);
	
	default Page<ProductResponse> getAllProductDetailsWithSpecsFilterByKeyword(Pageable pageable, String searchKeyword) {
		Page<ProductResponse> productResponses = getAllProductsByKeyword(pageable,searchKeyword);

	    for (ProductResponse response : productResponses) {
	        Long productId = response.getProductId();
	        List<Map<String, String>> specs = getProductSpecsById(productId);
	        response.setProductSpecs(specs);
	    }

	    return productResponses;
	}

}


