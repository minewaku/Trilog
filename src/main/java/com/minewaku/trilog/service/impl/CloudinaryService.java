package com.minewaku.trilog.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.minewaku.trilog.dto.common.response.CloudinaryResponse;
import com.minewaku.trilog.service.ICloudinaryService;
import com.minewaku.trilog.util.FileUploadUtil;

import jakarta.transaction.Transactional;

@Service
public class CloudinaryService implements ICloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    @Transactional
    public CloudinaryResponse uploadFile(MultipartFile file, String folder) {
        try {
            final Map result = cloudinary.uploader().upload(file.getBytes(), Map.of("folder", folder, "original_filename", FileUploadUtil.getFileName(file.getOriginalFilename())));
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
