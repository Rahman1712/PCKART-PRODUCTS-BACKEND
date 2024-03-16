package com.ar.pckart.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.InflaterInputStream;


public class ImageUtils {

	public static byte[] compress(byte[] data) {
	    try {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream, new Deflater(Deflater.BEST_COMPRESSION));
	        deflaterOutputStream.write(data);
	        deflaterOutputStream.close();
	        return outputStream.toByteArray();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public static byte[] decompress(byte[] compressedData) {
	    try {
	        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
	        InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream);
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = inflaterInputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, length);
	        }
	        inflaterInputStream.close();
	        return outputStream.toByteArray();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}


}