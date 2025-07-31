package com.minewaku.trilog.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.minewaku.trilog.dto.Media.MediaPostDTO;
import com.minewaku.trilog.dto.Media.SavedMediaPostDTO;
import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.Post.SavedPostDTO;
import com.minewaku.trilog.dto.common.response.CloudinaryResponse;
import com.minewaku.trilog.dto.common.response.StatusResponse;
import com.minewaku.trilog.entity.MediaPost;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.mapper.MediaMapper;
import com.minewaku.trilog.mapper.PostMapper;
import com.minewaku.trilog.service.impl.CloudinaryService;
import com.minewaku.trilog.service.impl.MediaPostService;
import com.minewaku.trilog.service.impl.PostService;
import com.minewaku.trilog.util.FileUploadUtil;
import com.minewaku.trilog.util.MessageUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UploadPostFacade {
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired 
	private PostService postSerivce;
	
	@Autowired
	private MediaPostService mediaPostService;
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private MediaMapper mediaMapper;
	
    @Transactional
	public PostDTO uploadPost(SavedPostDTO dto, List<MultipartFile> files) {
		Post createdPost = postMapper.savedPostDtoToEntity(dto);		

		try {
			if (files != null && !files.isEmpty()) {
				int order = 0;
				for (MultipartFile file : files) {
					if (file != null && !file.isEmpty()) {
						FileUploadUtil.assertMediaAllowed(file);
						CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/posts/images");
						SavedMediaPostDTO savedMediaPostDto = SavedMediaPostDTO.builder()
									.postId(createdPost.getId())
									.publicId(cloudinaryResponse.getPublicId())
									.secureUrl(cloudinaryResponse.getSecureUrl())
									.displayOrder(order)
									.build();
						order++;
						MediaPost mediaPost = mediaMapper.mediaPostDtoToMediaPostEntity(mediaPostService.save(savedMediaPostDto));
						
						createdPost.getMedia().add(mediaPost);
					}
				}
			}
			
		return postSerivce.create(postMapper.entityToDto(createdPost));
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
