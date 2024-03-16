package com.ar.pckart.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.pckart.model.ImagesData;

import jakarta.transaction.Transactional;

@Repository
public interface ImagesDataRepo extends JpaRepository<ImagesData, Long> {

	Optional<ImagesData> findByProductId(Long id);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM ImagesData i WHERE i.productId = :productid")
	void deleteImageData(@Param(value = "productid") Long productid);


	
}

