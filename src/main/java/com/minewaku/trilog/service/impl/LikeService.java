package com.minewaku.trilog.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import jakarta.transaction.Transactional;

@Service
public class LikeService implements ILikeService {
	
	@Autowired
	private ErrorUtil errorUtil;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private LikeMapper likeMapper;
	
	@Override
	public Page<LikeDTO> findAll(Map<String, String> params, Pageable pageable) {
		return likeRepository.findAll(pageable).map(likeMapper::entityToDto);
	}
	
	@Override
	@Transactional
	public List<Like> saveAll(List<Like> entities) {
	    if (entities == null || entities.isEmpty()) {
	        return List.of();
	    }

	    List<Like> resultLikes = new java.util.ArrayList<>();

	    for (Like like : entities) {
	        LikeId id = LikeId.builder()
	                .postId(like.getPost().getId())
	                .userId(like.getUser().getId())
	                .build();

	        if (likeRepository.existsById(id)) {
	            // Nếu tồn tại thì xóa
	            likeRepository.deleteById(id);
	        } else {
	            // Nếu chưa tồn tại thì thêm
	            resultLikes.add(likeRepository.save(like));
	        }
	        
	        LogUtil.LOGGER.error("like: {}", like);
	    }

	    return resultLikes;
	}


	@Override
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
        
        boolean exists = redisUtil.keyExists(RedisUtil.CACHE_PREFIX.LIKE_POST, compositeKey);
        
		if (!exists) {
			DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			String value = LocalDateTime.now().format(formatter);
			redisUtil.setValue(RedisUtil.CACHE_PREFIX.LIKE_POST, compositeKey, value);
		} else {
			redisUtil.deleteKey(RedisUtil.CACHE_PREFIX.LIKE_POST, compositeKey);
		}
	}
}
