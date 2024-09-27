package com.minewaku.trilog.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.minewaku.trilog.dto.response.CloudinaryResponse;
import com.minewaku.trilog.util.FileUploadUtil;

import jakarta.transaction.Transactional;

@Service
public class CloudinaryService {
    
    // @Autowired
    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "duss3fi7q",
        "api_key", "852862211451736",
        "api_secret", "plHJmWuVZoeqBVADB0Vz0eN3-iI",
        "secure", true));
    
    @Transactional
    public CloudinaryResponse uploadFile(MultipartFile file, String folder) {
        try {
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

    @Transactional
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
