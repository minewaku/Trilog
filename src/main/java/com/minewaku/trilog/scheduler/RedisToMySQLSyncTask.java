package com.minewaku.trilog.scheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.minewaku.trilog.entity.Like;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.entity.EmbededId.LikeId;
import com.minewaku.trilog.service.impl.LikeService;
import com.minewaku.trilog.util.LogUtil;
import com.minewaku.trilog.util.RedisUtil;

@Component
public class RedisToMySQLSyncTask {
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private LikeService	 likeService;
	
	@Scheduled(cron = "0 * * * * ?") 
    public void migrateLikeFromCacheToDb() {
		 Set<String> likeAddKeys = redisUtil.scanKeysByPrefix(RedisUtil.CACHE_PREFIX.LIKE_POST_ADD);
		 Set<String> likeDeleteKeys = redisUtil.scanKeysByPrefix(RedisUtil.CACHE_PREFIX.LIKE_POST_DELETE);
	
		 List<Like> likesToAdd = new ArrayList<>();
		 List<LikeId> likesToDelete = new ArrayList<>();
		 
		 for (String key : likeAddKeys) {
	        String composite = redisUtil.getKeyWithoutPrefix(key);
	        String[] parts = composite.split(":");
	        Integer userId = Integer.parseInt(parts[0]);
	        Integer postId = Integer.parseInt(parts[1]);
	        LocalDateTime createdDate = (LocalDateTime) redisUtil.getValue(RedisUtil.CACHE_PREFIX.LIKE_POST_ADD, composite);

	        likesToAdd.add(new Like(new LikeId(userId, postId), 
	        		User.builder().id(userId).build(), 
	        		Post.builder().id(postId).build(),
	        		createdDate));
	    }

	    for (String key : likeDeleteKeys) {
	        String composite = redisUtil.getKeyWithoutPrefix(key);
	        String[] parts = composite.split(":");
	        Integer userId = Integer.parseInt(parts[0]);
	        Integer postId = Integer.parseInt(parts[1]);

	        likesToDelete.add(new LikeId(userId, postId));
	    }
	    
	    likeService.saveAll(likesToAdd);
	    likeService.deleteAll(likesToDelete);
	}
}
