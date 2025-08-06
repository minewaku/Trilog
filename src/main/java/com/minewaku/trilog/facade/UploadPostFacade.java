package com.minewaku.trilog.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.minewaku.trilog.dto.Media.SavedMediaDTO;
import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.Post.SavedPostDTO;
import com.minewaku.trilog.dto.common.response.CloudinaryResponse;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.entity.MediaPost;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.mapper.MediaMapper;
import com.minewaku.trilog.mapper.PostMapper;
import com.minewaku.trilog.repository.MediaPostRepository;
import com.minewaku.trilog.repository.PostRepository;
import com.minewaku.trilog.service.IMediaPostService;
import com.minewaku.trilog.service.IMediaService;
import com.minewaku.trilog.service.impl.CloudinaryService;
import com.minewaku.trilog.service.impl.PostService;
import com.minewaku.trilog.util.FileUploadUtil;
import com.minewaku.trilog.util.LogUtil;

@Service
public class UploadPostFacade {
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private PostService postSerivce;
	
	@Autowired 
	private PostRepository postRepository;
	
	@Autowired
	private MediaPostRepository mediaPostRepository;	
	
	@Autowired
	private IMediaPostService mediaPostService;
	
	@Autowired
	private IMediaService mediaService;
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private MediaMapper mediaMapper;
	
    @Transactional
	public PostDTO uploadPost(SavedPostDTO dto, List<MultipartFile> files) {
		try {
			Post savedPost = postSerivce.createForServices(dto);
			
			if (files != null && !files.isEmpty()) {
				int order = 0;
				for (MultipartFile file : files) {
					if (file != null && !file.isEmpty()) {
						FileUploadUtil.assertMediaAllowed(file);
						CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/posts/images");
//						LogUtil.LOGGER.info(cloudinaryResponse.toString());
						SavedMediaDTO savedMediaDto = SavedMediaDTO.builder()
								.publicId(cloudinaryResponse.getPublicId())
								.secureUrl(cloudinaryResponse.getSecureUrl())
								.build();
						
					    Media mediaDto = mediaService.createForServices(savedMediaDto);
					    LogUtil.LOGGER.info(mediaDto.toString());
					    
						MediaPost savedMediaPostDto = MediaPost.builder()
								.media(mediaDto)
								.post(savedPost)
								.displayOrder(order)
								.build();
						
//						LogUtil.LOGGER.info(savedMediaPostDto.toString());
						order++;
						
						savedPost.getMedia().add(savedMediaPostDto);
						
						savedMediaPostDto.builder().post(savedPost).build();
//						MediaPost mediaPost = mediaMapper.mediaPostDtoToMediaPostEntity(mediaPostService.save(savedMediaPostDto));
//						mediaPostRepository.save(savedMediaPostDto);
					} else {
						throw new RuntimeException("File is empty or null");
					}
				}
			}
			
		return postMapper.entityToDto(postRepository.save(savedPost));
//		return postMapper.entityToDto(savedPost);
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
