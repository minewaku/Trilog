package com.minewaku.trilog.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.PostDTO;
import com.minewaku.trilog.dto.response.StatusResponse;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.mapper.PostMapper;
import com.minewaku.trilog.repository.PostRepository;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.service.IPostService;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.MessageUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostService implements IPostService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired	
	private UserRepository userRepository;

	@Autowired
	private PostMapper mapper;
	
    @Autowired
    private ErrorUtil errorUtil;
	
	@Override
	public PostDTO create(PostDTO post) {
	 try {
		 	User user = userRepository.findById(post.getUserId()).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
		 	Post entity = mapper.dtoToEntity(post);
		 	entity.setUser(user);
		 	entity.setUsername(user.getName());
           
            Post savedPost = postRepository.save(entity); 
            return mapper.entityToDto(savedPost);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
	}
	
	@Override
    public PostDTO update(int id, PostDTO Post) {
        try {
            postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageUtil.getMessage("error.get.post"))); 
            Post savedPost = postRepository.save(mapper.dtoToEntity(Post)); 
            return mapper.entityToDto(savedPost);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public StatusResponse delete(List<Integer> ids) {
        try {
            postRepository.softDeletePosts(ids); 
            return new StatusResponse(MessageUtil.getMessage("success.delete"), ZonedDateTime.now(ZoneId.of("Z")));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
