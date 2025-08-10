package com.minewaku.trilog.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.User.UserDTO;
import com.minewaku.trilog.entity.Like;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.entity.EmbededId.LikeId;
import com.minewaku.trilog.service.impl.LikeService;
import com.minewaku.trilog.service.impl.PostService;
import com.minewaku.trilog.service.impl.UserService;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.LogUtil;
import com.minewaku.trilog.util.RedisUtil;

import jakarta.transaction.Transactional;

@Component
public class RedisToMySQLSyncTask {
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private ErrorUtil errorUtil;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LikeService	 likeService;
	
	
	
	@Scheduled(cron = "0 * * * * ?") 
	@Transactional
    public void migrateLikeFromCacheToDb() {

		Set<String> likeKeys = redisUtil.scanKeysByPrefix(RedisUtil.CACHE_PREFIX.LIKE_POST);
	
		List<Like> likesToAdd = new ArrayList<>();
		 
		for (String key : likeKeys) {
	       String composite = redisUtil.getKeyWithoutPrefix(key);
	       String[] parts = composite.split(":");
	       Integer userId = Integer.parseInt(parts[0]);
	       Integer postId = Integer.parseInt(parts[1]);
	       String redisValue = (String) redisUtil.getValue(RedisUtil.CACHE_PREFIX.LIKE_POST, composite);
	        
	       DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	       LocalDateTime createdDate = LocalDateTime.parse(redisValue, formatter);

    	   PostDTO post = postService.findById(postId);
    	   UserDTO user = userService.findById(userId);
	       likesToAdd.add(new Like(new LikeId(userId, postId), 
	       		User.builder().id(userId).build(), 
	       		Post.builder().id(postId).build(),
	       		createdDate));

    	   redisUtil.deleteKey(RedisUtil.CACHE_PREFIX.LIKE_POST, composite);

		}
	    
	    likeService.saveAll(likesToAdd);
	}
}
