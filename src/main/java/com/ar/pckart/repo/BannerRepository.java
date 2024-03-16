package com.ar.pckart.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ar.pckart.model.Banner;

import jakarta.transaction.Transactional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, String> {

	@Modifying
	@Transactional
	@Query(value = "UPDATE Banner b set b.enabled = :enabled WHERE b.id = :id")
	void updateBannerEnableById(String id, boolean enabled);
	
}