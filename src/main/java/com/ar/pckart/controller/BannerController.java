package com.ar.pckart.controller;

import java.io.IOException;
import java.util.List;

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

import com.ar.pckart.model.Banner;
import com.ar.pckart.service.BannerService;

@RestController
@RequestMapping("/pckart/api/v1/banners")
public class BannerController {
	
    @Autowired  private BannerService bannerService;

    @GetMapping("/get/allBanners")
    public ResponseEntity<?> getAllBanners(){
        List<Banner> banners = bannerService.findAll();
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/get/byId/{id}")
    public ResponseEntity<?> getBannerByid(@PathVariable String id){
        Banner banner = bannerService.getBannerById(id);
        return ResponseEntity.ok(banner);
    }

    @PostMapping("/auth/save")
    public ResponseEntity<?> saveBanner(
    		@RequestPart("banner") Banner banner,
    		@RequestParam("file") MultipartFile file
    		) {
		try {
			Banner bannerSaved = bannerService.save(banner, file);
			System.err.println(bannerSaved);
			return ResponseEntity.ok("banner saved");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
    }

    @PutMapping("/auth/update/{id}")
    public ResponseEntity<?> saveBanner(
    		@PathVariable("id") String id,
    		@RequestPart("banner") Banner banner,
	        @RequestParam("file") MultipartFile file) {

		try {
			bannerService.update(id, banner, file);
			return ResponseEntity.ok("Banner is updated");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
    }

    @DeleteMapping("/auth/delete/byid/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
       return ResponseEntity.ok(bannerService.delete(id));
    }

    @PutMapping("/auth/update/enabled/{id}")
    public ResponseEntity<String> updateBannerEnabledById(
    		@PathVariable("id") String id,
    		@RequestParam("enabled") boolean enabled) {
        return ResponseEntity.ok(bannerService.updateBannerEnabledById(id, enabled));
    }
    
    @GetMapping
    public ResponseEntity<?> work(){
        return ResponseEntity.ok("Banners");
    }

}
