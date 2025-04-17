package com.minewaku.trilog.facade;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.response.CloudinaryResponse;
import com.minewaku.trilog.service.impl.CloudinaryService;
import com.minewaku.trilog.service.impl.MediaService;
import com.minewaku.trilog.service.impl.UserService;
import com.minewaku.trilog.util.FileUploadUtil;

@Component
public class UploadFileFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Transactional
    public MediaDTO uploadUserImage(int userId, MultipartFile file)
    {
        try {
            MediaDTO existingImage = userService.getImage(userId);
            if(existingImage != null) {
                FileUploadUtil.assertImageAllowed(file, FileUploadUtil.IMAGE_PATTERN);
                CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/users/images"); 
                MediaDTO savedFile = mediaService.update(existingImage.getId(), MediaDTO.builder()
                                                    .id(existingImage.getId())
                                                    .publicId(cloudinaryResponse.getPublicId())
                                                    .secureUrl(cloudinaryResponse.getSecureUrl())
                                                    .build());
                cloudinaryService.deleteFile(existingImage.getPublicId());
                return userService.updateImage(userId, savedFile.getId());
            } else {
                FileUploadUtil.assertImageAllowed(file, FileUploadUtil.IMAGE_PATTERN);
                CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/users/images");  
                MediaDTO savedFile = mediaService.create(MediaDTO.builder()
                                                            .publicId(cloudinaryResponse.getPublicId())
                                                            .secureUrl(cloudinaryResponse.getSecureUrl())
                                                            .build());
                return userService.updateImage(userId, savedFile.getId());
            }
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // @Transactional
    // public MediaDTO uploadUserImage(int userId, MultipartFile file)
    // {
    //     try {
    //         FileUploadUtil.assertImageAllowed(file, FileUploadUtil.IMAGE_PATTERN);
    //         UserDTO existingUser = userService.findById(userId);
    //         CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/users/images");  
    //         MediaDTO savedFile = mediaService.create(MediaDTO.builder()
    //                                                     .publicId(cloudinaryResponse.getPublicId())
    //                                                     .secureUrl(cloudinaryResponse.getSecureUrl())
    //                                                     .build());
    //         return userService.updateImage(existingUser.getId(), savedFile.getId());
    //     } catch(Exception e) {
    //         throw new RuntimeException(e.getMessage(), e);
    //     }
    // }

    @Transactional
    public MediaDTO updateUserImage(int userId, MultipartFile file)
    {
        try {
            MediaDTO existingImage = userService.getImage(userId);
            FileUploadUtil.assertImageAllowed(file, FileUploadUtil.IMAGE_PATTERN);
            cloudinaryService.deleteFile(existingImage.getPublicId());
            CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/users/images"); 
            MediaDTO savedFile = mediaService.update(userId, MediaDTO.builder()
                                                .publicId(cloudinaryResponse.getPublicId())
                                                .secureUrl(cloudinaryResponse.getSecureUrl())
                                                .build());
                                                
            return userService.updateImage(userId, savedFile.getId());
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Transactional
    public MediaDTO uploadUserCover(int userId, MultipartFile file)
    {
        try {
            MediaDTO existingImage = userService.getCover(userId);
            if(existingImage != null) {
                FileUploadUtil.assertImageAllowed(file, FileUploadUtil.IMAGE_PATTERN);
                CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/users/covers"); 
                MediaDTO savedFile = mediaService.update(existingImage.getId(), MediaDTO.builder()
                                                    .id(existingImage.getId())
                                                    .publicId(cloudinaryResponse.getPublicId())
                                                    .secureUrl(cloudinaryResponse.getSecureUrl())
                                                    .build());
                cloudinaryService.deleteFile(existingImage.getPublicId());
                return userService.updateCover(userId, savedFile.getId());
            } else {
                FileUploadUtil.assertImageAllowed(file, FileUploadUtil.IMAGE_PATTERN);
                CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/users/covers");  
                MediaDTO savedFile = mediaService.create(MediaDTO.builder()
                                                            .publicId(cloudinaryResponse.getPublicId())
                                                            .secureUrl(cloudinaryResponse.getSecureUrl())
                                                            .build());
                return userService.updateCover(userId, savedFile.getId());
            }
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // @Transactional
    // public MediaDTO uploadUserCover(int userId, MultipartFile file)
    // {
    //     try {
    //         FileUploadUtil.assertImageAllowed(file, FileUploadUtil.IMAGE_PATTERN);
    //         UserDTO existingUser = userService.findById(userId);
    //         CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/users/covers");  
    //         MediaDTO savedFile = mediaService.create(MediaDTO.builder()
    //                                                     .publicId(cloudinaryResponse.getPublicId())
    //                                                     .secureUrl(cloudinaryResponse.getSecureUrl())
    //                                                     .build());
    //         return userService.updateCover(existingUser.getId(), savedFile.getId());
    //     } catch(Exception e) {
    //         throw new RuntimeException(e.getMessage(), e);
    //     }
    // }

    @Transactional
    public MediaDTO updateUserCover(int userId, MultipartFile file)
    {
        try {
            MediaDTO existingImage = userService.getCover(userId);
            FileUploadUtil.assertImageAllowed(file, FileUploadUtil.IMAGE_PATTERN);
            cloudinaryService.deleteFile(existingImage.getPublicId());
            CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/users/covers");  
            MediaDTO savedFile = mediaService.update(existingImage.getId(), MediaDTO.builder()
                                                .id(existingImage.getId())
                                                .publicId(cloudinaryResponse.getPublicId())
                                                .secureUrl(cloudinaryResponse.getSecureUrl())
                                                .build());
            
            return userService.updateCover(userId, savedFile.getId());
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
