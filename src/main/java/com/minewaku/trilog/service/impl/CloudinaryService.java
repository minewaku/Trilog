package com.minewaku.trilog.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.minewaku.trilog.dto.common.response.CloudinaryResponse;
import com.minewaku.trilog.service.ICloudinaryService;
import com.minewaku.trilog.util.FileUploadUtil;
import com.minewaku.trilog.util.LogUtil;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class CloudinaryService implements ICloudinaryService {
	
	@Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Value("${cloudinary.secure}")
    private Boolean secure;
    
    private Cloudinary cloudinary;
    
    @PostConstruct
    public void init() {
    	LogUtil.LOGGER.info("cloud_name: " + cloudName);
    	LogUtil.LOGGER.info("api_key: " + apiKey);
    	LogUtil.LOGGER.info("api_secret: " + apiSecret);
    	LogUtil.LOGGER.info("secure: " + secure);
    	
    	this.cloudinary = new Cloudinary(ObjectUtils.asMap(
    	            "cloud_name", cloudName,
    	            "api_key", apiKey,
    	            "api_secret", apiSecret,
    	            "secure", secure));
    }
    
    @Override
    @Transactional
    public CloudinaryResponse uploadFile(MultipartFile file, String folder) {
        try {
        	LogUtil.LOGGER.info("cloud_name: " + cloudName);
        	LogUtil.LOGGER.info("api_key: " + apiKey);
        	LogUtil.LOGGER.info("api_secret: " + apiSecret);
        	LogUtil.LOGGER.info("secure: " + secure);
        	
            final Map result = cloudinary.uploader().upload(file.getBytes(), Map.of("asset_folder", folder, "original_filename", FileUploadUtil.getFileName(file.getOriginalFilename())));
            final String secureUrl = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder()
                                    .publicId(publicId)
                                    .secureUrl(secureUrl)
                                    .build();
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
