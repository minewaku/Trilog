package com.minewaku.trilog.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    // Key Prefixes
    public static class CACHE_PREFIX {
        public static final String IP_RATE_LIMIT_BUCKETS = "ipBuckets:";
        public static final String REFRESH_TOKENS = "refreshTokens:";
        public static final String EMAIL_VERIFICATION_TOKENS = "emailVerificationTokens:";
        public static final String EMAIL_VERIFICATION_PENDING = "emailVerificationPending:";
        public static final String LIKE_POST= "like_post:";
        public static final String VIEW_POST = "view_post:";
    }
    
    public String getKeyWithoutPrefix(String fullKey) {
        int indexOfColon = fullKey.indexOf(':');
        if (indexOfColon != -1) {
            return fullKey.substring(indexOfColon + 1); // Return the part after the colon
        }
        return fullKey; // Return the full key if there's no colon
    }
    
	public Set<Object> getKeysByPrefix(String prefix) {
		return redisTemplate.keys(prefix + "*");
	}
	
	public Set<String> scanKeysByPrefix(String prefix) {
        Set<String> keys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(prefix + "*").count(1000).build();

        RedisConnection connection = redisTemplate.getRequiredConnectionFactory().getConnection();

        if (connection instanceof RedisKeyCommands) {
            RedisKeyCommands keyCommands = (RedisKeyCommands) connection;
            try (Cursor<byte[]> cursor = keyCommands.scan(options)) {
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next()));
                }
            }
        }

        return keys;
    }
    
	public String makeCompositeKey(String[] keys) {
		return String.join(":", keys);
	}

    
    // Set a value in Redis with a timeout
    public void setValue(String prefix, String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(prefix + key, value, timeout, timeUnit);
        } catch (Exception e) {
            throw new RuntimeException("Error setting value in Redis", e);
        }
    }
    
    // Set a value in Redis without timeout
	public void setValue(String prefix, String key, Object value) {
		try {
			redisTemplate.opsForValue().set(prefix + key, value);
		} catch (Exception e) {
			throw new RuntimeException("Error setting value in Redis", e);
		}
	}	
	
	
	// Set a list value in Redis with a timeout
	public void setListValue(String prefix, String key, Object value, long timeout, TimeUnit timeUnit) {
		try {
			redisTemplate.opsForList().rightPushAll(prefix + key, value, timeout, timeUnit);
		} catch (Exception e) {
			throw new RuntimeException("Error setting list value in Redis", e);
		}
	}
	
    // Set a list value in Redis without timeout
	public void setListValue(String prefix, String key, Object value) {
		try {
			redisTemplate.opsForList().rightPushAll(prefix + key, value);
		} catch (Exception e) {
			throw new RuntimeException("Error setting list value in Redis", e);
		}
	}


    // Get a value from Redis
    public Object getValue(String prefix, String key) {
        try {
            return redisTemplate.opsForValue().get(prefix + key);
        } catch (Exception e) {
            throw new RuntimeException("Error getting value from Redis", e);
        }
    }

    // Delete a key from Redis
    public void deleteKey(String prefix, String key) {
        try {
            redisTemplate.delete(prefix + key);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting key from Redis", e);
        }
    }

    // Check if a key exists in Redis
    public boolean keyExists(String prefix, String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(prefix + key));
        } catch (Exception e) {
            throw new RuntimeException("Error checking key existence in Redis", e);
        }
    }
}