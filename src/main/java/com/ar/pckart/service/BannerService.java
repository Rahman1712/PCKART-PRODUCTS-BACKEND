package com.ar.pckart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ar.pckart.model.Banner;
import com.ar.pckart.repo.BannerRepository;
import com.ar.pckart.util.ImageUtils;

import java.io.IOException;
import java.util.List;

@Service
public class BannerService {
	
    @Autowired
    private BannerRepository bannerRepository;

    public List<Banner> findAll() {
        List<Banner> banners = bannerRepository.findAll();
        banners.forEach(banner -> {
        	banner.setBannerImage(ImageUtils.decompress(banner.getBannerImage()));
        });
        return banners;
    }

    public Banner getBannerById(String id) {
        Banner banner = bannerRepository.findById(id).orElse(null);
        banner.setBannerImage(ImageUtils.decompress(banner.getBannerImage()));
        return banner;
    }

    public Banner save(Banner banner , MultipartFile file) throws IOException {
    	banner.setBannerImage(ImageUtils.compress(file.getBytes()));
    	banner.setImageName(file.getOriginalFilename());
    	banner.setImageType(file.getContentType());
        return bannerRepository.save(banner);  
    }
    
    public Banner update(String id, Banner banner , MultipartFile file) throws IOException {
    	banner.setId(id);
    	banner.setBannerImage(ImageUtils.compress(file.getBytes()));
        return bannerRepository.save(banner);
    }

    public String delete(String id) {
        bannerRepository.deleteById(id);
        return "deleted";
    }

    public String updateBannerEnabledById(String id,boolean enable) {
       bannerRepository.updateBannerEnableById(id,enable);
       return "enabled Updated";
    }

}