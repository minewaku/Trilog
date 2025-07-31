package com.minewaku.trilog.service;

import org.springframework.web.multipart.MultipartFile;

import com.minewaku.trilog.dto.common.response.CloudinaryResponse;

public interface ICloudinaryService {
	CloudinaryResponse uploadFile(MultipartFile file, String folder);
	void deleteFile(String publicId);
}
