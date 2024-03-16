package com.ar.pckart.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.pckart.dto.BrandResponse;
import com.ar.pckart.model.Brand;

import jakarta.transaction.Transactional;

@Repository
public interface BrandRepo extends JpaRepository<Brand, Long>{
	
	@Query(value = "CREATE FULLTEXT INDEX idx_brand_name ON brands(brand_name)", nativeQuery = true)
	void createBrandNameFullTextIndex();

	@Modifying
	@Transactional
	@Query("update Brand u set u.name = :name, u.image= :image, u.imageName = :imageName, u.imageType = :imageType where u.id = :id")
	void updateBrand(@Param(value = "id") Long id,
			@Param(value = "name") String name,
			@Param(value = "image") byte[] file,
			@Param(value = "imageName") String imageName,
			@Param(value = "imageType") String imageType
			);
	
	
	@Query("SELECT new com.ar.pckart.dto.BrandResponse(b.id, b.name) FROM Brand b")
	public List<BrandResponse> allBrandsIdAndName();
	
	@Query("SELECT b FROM Brand b")
	public List<Brand> allBrandsQuery();

	@Query("SELECT new com.ar.pckart.dto.BrandResponse(b.id, b.name) "
			+ "FROM Brand b WHERE b.id = ?1")
	Optional<BrandResponse> findByIdResponse(Long id);

	@Query("SELECT b FROM Brand b WHERE b.id = ?1")
	Optional<Brand> findBrandByBrandId(Long id);

	@Query("SELECT b FROM Brand b WHERE b.name IN ?1")
	List<Brand> findBrandByFilter(List<String> brands);

	public List<Brand> findAllByOrderByAddedatDesc();
}

