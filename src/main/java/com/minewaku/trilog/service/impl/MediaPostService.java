package com.minewaku.trilog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.Media.MediaPostDTO;
import com.minewaku.trilog.dto.Media.SavedMediaPostDTO;
import com.minewaku.trilog.entity.MediaPost;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.mapper.MediaMapper;
import com.minewaku.trilog.repository.MediaPostRepository;
import com.minewaku.trilog.repository.PostRepository;
import com.minewaku.trilog.service.IMediaPostService;
import com.minewaku.trilog.util.ErrorUtil;

@Service
public class MediaPostService implements IMediaPostService{
	
    @Autowired
    private MediaPostRepository mediaPostRepository;
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MediaMapper mediaMapper;
    
    @Autowired
    private ErrorUtil errorUtil;
    
    @Override
    public MediaPostDTO save(SavedMediaPostDTO media) {
        try {
        	Post owner = postRepository.findById(media.getPostId())
        			.orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.POST_NOT_FOUND));
        	
        	MediaPost savedMedia = mediaMapper.savedMediaPostDtoToEntity(media);
        	savedMedia.setPost(owner);
        			
			return mediaMapper.mediaPostEntityToMediaPostDto(mediaPostRepository.save(savedMedia));
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(List<Integer> ids) {
        try {
        	mediaPostRepository.deleteAllById(ids);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
