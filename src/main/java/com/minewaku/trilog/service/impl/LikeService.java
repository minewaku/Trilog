package com.minewaku.trilog.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.LikeDTO;
import com.minewaku.trilog.entity.Like;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.entity.EmbededId.LikeId;
import com.minewaku.trilog.mapper.LikeMapper;
import com.minewaku.trilog.repository.LikeRepository;
import com.minewaku.trilog.repository.PostRepository;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.service.ILikeService;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.LogUtil;
import com.minewaku.trilog.util.RedisUtil;
import com.minewaku.trilog.util.SecurityUtil;

@Service
public class LikeService implements ILikeService {
	
	@Autowired
	private ErrorUtil errorUtil;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private LikeMapper likeMapper;
	
	public Page<LikeDTO> findAll(Pageable pageable, Map<String, String> params) {
		return likeRepository.findAll(pageable).map(likeMapper::entityToDto);
	}
	
	public List<Like> saveAll(List<Like> entities) {
		LogUtil.LOGGER.info("migrated post likes: " + entities.stream().map(Object::toString).collect(Collectors.joining(", ")));
		return likeRepository.saveAll(entities);
	}
	
	public void deleteAll(List<LikeId> ids) {
		LogUtil.LOGGER.info("migrated post likes: " + ids.stream().map(Object::toString).collect(Collectors.joining(", ")));
		likeRepository.deleteAllById(ids);
	}
	
	
	public void cachedLikePost(Integer postId) {
		User userDetails = (User) SecurityUtil.getPrincipal(); 
		
		User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.POST_NOT_FOUND));
		
        String compositeKey = redisUtil
    		.makeCompositeKey(new String[]
    			{
    				user.getId().toString(), 
    				post.getId().toString()
    			}
    		);
        
        Object addValue = redisUtil.getValue(RedisUtil.CACHE_PREFIX.LIKE_POST_ADD, compositeKey);
        Object deleteValue = redisUtil.getValue(RedisUtil.CACHE_PREFIX.LIKE_POST_DELETE, compositeKey);
        
		if (addValue == null && deleteValue == null) {
			redisUtil.setValue(RedisUtil.CACHE_PREFIX.LIKE_POST_ADD, compositeKey, LocalDateTime.now());
			
			try {
				Integer likes = (Integer) redisUtil.getValue(RedisUtil.CACHE_PREFIX.LIKE_POST_QUANTITY, post.getId().toString());
				if(likes == null) {
					redisUtil.setValue(RedisUtil.CACHE_PREFIX.LIKE_POST_QUANTITY, post.getId().toString(), post.getLikes());
				} else {
					redisUtil.setValue(RedisUtil.CACHE_PREFIX.LIKE_POST_QUANTITY, post.getId().toString(), likes + 1);
				}
			} catch (ClassCastException e) {
				throw errorUtil.ERROR_DETAILS.get(errorUtil.INTERNAL_SERVER_ERROR);
			}
		} else if(addValue == null && deleteValue != null ) {
			redisUtil.deleteKey(RedisUtil.CACHE_PREFIX.LIKE_POST_DELETE, compositeKey);
		} else if (addValue != null && deleteValue == null) {
			throw errorUtil.ERROR_DETAILS.get(errorUtil.REQUEST_IN_PROGRESS);
		}
	}
	
	public void uncachedLikePost(Integer postId) {
		User userDetails = (User) SecurityUtil.getPrincipal();  
		
		User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.POST_NOT_FOUND));
        
		String compositeKey = redisUtil
    		.makeCompositeKey(new String[]
    			{
    				user.getId().toString(), 
    				post.getId().toString()
    			}
    		);
		
        Object addValue = redisUtil.getValue(RedisUtil.CACHE_PREFIX.LIKE_POST_ADD, compositeKey);
        Object deleteValue = redisUtil.getValue(RedisUtil.CACHE_PREFIX.LIKE_POST_DELETE, compositeKey);
        
		if (addValue == null && deleteValue == null) {
			redisUtil.setValue(RedisUtil.CACHE_PREFIX.LIKE_POST_DELETE, compositeKey, LocalDateTime.now());
			
			try {
				Integer likes = (Integer) redisUtil.getValue(RedisUtil.CACHE_PREFIX.LIKE_POST_QUANTITY, post.getId().toString());
				if(likes == null) {
					redisUtil.setValue(RedisUtil.CACHE_PREFIX.LIKE_POST_QUANTITY, post.getId().toString(), post.getLikes());
				} else {
					redisUtil.setValue(RedisUtil.CACHE_PREFIX.LIKE_POST_QUANTITY, post.getId().toString(), likes - 1);
				}
			} catch (ClassCastException e) {
				throw errorUtil.ERROR_DETAILS.get(errorUtil.INTERNAL_SERVER_ERROR);
			}
		} else if (addValue != null && deleteValue == null) {
			redisUtil.deleteKey(RedisUtil.CACHE_PREFIX.LIKE_POST_DELETE, compositeKey);
		} else if (addValue == null && deleteValue != null) {
			throw errorUtil.ERROR_DETAILS.get(errorUtil.REQUEST_IN_PROGRESS);
		}
	}
}
