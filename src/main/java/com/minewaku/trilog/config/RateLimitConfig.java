package com.minewaku.trilog.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

@Configuration
public class RateLimitConfig {

    @Bean
    @Primary
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean
    public RedisClient redisClient() {
        RedisProperties properties = new RedisProperties();
        RedisURI redisUri = RedisURI.builder()
            .withHost(properties.getHost())
            .withPort(properties.getPort())
            .withTimeout(Duration.ofSeconds(10))
            .withSsl(false)
            .build();
        return RedisClient.create(redisUri);
    }

    @Bean
    public ProxyManager<String> lettuceBasedProxyManager() {
        RedisClient redisClient = redisClient();
        StatefulRedisConnection<String, byte[]> redisConnection = redisClient
            .connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager.builderFor(redisConnection)
            .build();
    }
}
