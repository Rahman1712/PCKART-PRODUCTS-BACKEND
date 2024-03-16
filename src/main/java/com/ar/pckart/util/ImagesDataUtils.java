package com.ar.pckart.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.web.multipart.MultipartFile;

public class ImagesDataUtils {

	public static void imageToFolder(String folderPath, MultipartFile file, String imageName ) throws IOException {
		
		File fileToCreate = new File(folderPath+imageName);
		if(fileToCreate.createNewFile()) {
			System.out.println("File created "+fileToCreate.getAbsolutePath());
		}else {
			System.out.println("File already exists");
		}
		
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileToCreate));
		stream.write(file.getBytes());
		stream.close();
		
	}
	
	public static byte[] imageFromFolder(String path, String fileName) throws IOException {
//		byte[] image = Files.readAllBytes(new File(path+"\\"+fileName).toPath());
		byte[] image = Files.readAllBytes(new File(path+""+fileName).toPath());
		return image;
	}
}
