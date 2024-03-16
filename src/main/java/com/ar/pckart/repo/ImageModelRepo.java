package com.ar.pckart.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.pckart.model.ImageModel;

import jakarta.transaction.Transactional;

@Repository
public interface ImageModelRepo extends JpaRepository<ImageModel, Long>{

	@Modifying
    @Transactional
    @Query("DELETE FROM ImageModel im WHERE im IN :subImages")
    void deleteSubImages(@Param(value = "subImages") Set<ImageModel> subImages);

}
