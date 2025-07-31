package com.minewaku.trilog.util;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    public static final long MAX_IMAGE_SIZE = 1024 * 1024 * 20; // 20MB
    public static final long MAX_VIDEO_SIZE = 1024 * 1024 * 100; // 100MB

    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|jpeg|jpe|jfif|png|webp|gif))$)";
    public static final String VIDEO_PATTERN = "([^\\s]+(\\.(?i)(mp4|avi|mov|mpeg))$)";

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String FILE_NAME_FORMAT = "%s_%s";

    public static boolean isAllowedExtension(final String fileName, final String pattern) {
        final Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(fileName);
        return matcher.matches();   
    }

    public static void assertMediaAllowed(MultipartFile file) {
    	 final long size = file.getSize();
         final String fileName = file.getOriginalFilename();
         
		if (isAllowedExtension(fileName, IMAGE_PATTERN)) {
			if (size > MAX_IMAGE_SIZE) {
				throw new RuntimeException(MessageUtil.getMessage("cloudinary.invalid.media.size.image") + MAX_IMAGE_SIZE / 1024 / 1024 + "MB" + ". " + fileName);
			}
		} else if (isAllowedExtension(fileName, VIDEO_PATTERN)) {
			if (size > MAX_VIDEO_SIZE) {
				throw new RuntimeException(MessageUtil.getMessage("cloudinary.invalid.media.size.video") + MAX_VIDEO_SIZE / 1024 / 1024 + "MB" + ". " + fileName);
			}
		} else {
			throw new RuntimeException(MessageUtil.getMessage("cloudinary.invalid.media.type") + ". " + fileName);
		}
    }
    
    public static void assertImageAllowed(MultipartFile file, String pattern) {
        final long size = file.getSize();
        final String fileName = file.getOriginalFilename();
        
        if(size > MAX_IMAGE_SIZE) {
            throw new RuntimeException(MessageUtil.getMessage("cloudinary.invalid.media.size.image") + MAX_IMAGE_SIZE / 1024 /1024 + "MB" + ". " + fileName);
        }

        if (!isAllowedExtension(fileName, pattern)) {
            throw new RuntimeException(MessageUtil.getMessage("cloudinary.invalid.media.type.image") + ". " + fileName);
        }
    }

    public static void assertVideoAllowed(MultipartFile file, String pattern) {
        final long size = file.getSize();
        final String fileName = file.getOriginalFilename();
        
        if(size > MAX_IMAGE_SIZE) {
            throw new RuntimeException(MessageUtil.getMessage("cloudinary.invalid.media.size.video") + MAX_VIDEO_SIZE / 1024 / 1024 + "MB" + ". " + fileName);
        }

        if (!isAllowedExtension(fileName, pattern)) {
            throw new RuntimeException(MessageUtil.getMessage("cloudinary.invalid.media.type.video") + ". " + fileName);
        }
    }

    public static String getFileName(String originalFileName) {
        final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final String date = dateFormat.format(System.currentTimeMillis());
        return String.format(FILE_NAME_FORMAT, date, originalFileName);
    }

    public static byte[] compressFile(byte[] file) {
        Deflater deflater = new Deflater();
        deflater.setInput(file);
        deflater.setLevel(9);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while(!deflater.finished()) {
            int size = deflater.deflate(buffer);
            outputStream.write(buffer, 0, size);
        }

        try {
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] outputData = outputStream.toByteArray();
        return outputData;
    }
}
