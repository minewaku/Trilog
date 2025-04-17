package com.minewaku.trilog.service.impl;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.constant.RateLimitLevel;
import com.minewaku.trilog.constant.RateLimitStrategy;
import com.minewaku.trilog.util.ClientUtil;
import com.minewaku.trilog.util.RedisUtil;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class RateLimitService {

    public static final String USER_CACHE_PREFIX = "ips:";

    @Autowired
    private ClientUtil clientUtil;

    @Autowired
    public ProxyManager<String> lettuceBasedProxyManager;

    public Supplier<BucketConfiguration> bucketConfiguration(String limitLevel) {  
        return () -> BucketConfiguration.builder()
            .addLimit(RateLimitLevel.valueOf(limitLevel).getLimit())
            .build();
    }

    public Supplier<BucketConfiguration> bucketConfiguration(String limitStrategy, int requestLimit, int period) {
        return () -> BucketConfiguration.builder()
            .addLimit(RateLimitStrategy.valueOf(limitStrategy).getLimit(requestLimit, period))
            .build();
    }

    public Bucket redisBucket(HttpServletRequest request, String limitLevel) {
        String ipAddress = clientUtil.getClientIP(request);
        Supplier<BucketConfiguration> configurationLazySupplier = bucketConfiguration(limitLevel);
        ProxyManager<String> proxyManager = lettuceBasedProxyManager;

        return proxyManager.builder().build(RedisUtil.CACHE_PREFIX.IP_RATE_LIMIT_BUCKETS + ipAddress, configurationLazySupplier);
    }

    public Bucket redisBucket(HttpServletRequest request, String limitStrategy, int requestLimit, int period) {
        String ipAddress = clientUtil.getClientIP(request);
        Supplier<BucketConfiguration> configurationLazySupplier = bucketConfiguration(limitStrategy, requestLimit, period);
        ProxyManager<String> proxyManager = lettuceBasedProxyManager;

        return proxyManager.builder().build(RedisUtil.CACHE_PREFIX.IP_RATE_LIMIT_BUCKETS + ipAddress, configurationLazySupplier);
    }

    public Bucket redisBucket(String id, String limitLevel) {
        Supplier<BucketConfiguration> configurationLazySupplier = bucketConfiguration(limitLevel);
        ProxyManager<String> proxyManager = lettuceBasedProxyManager;

        return proxyManager.builder().build(id, configurationLazySupplier);
    }

    public Bucket redisBucket(String id, String limitStrategy, int requestLimit, int period) {
        Supplier<BucketConfiguration> configurationLazySupplier = bucketConfiguration(limitStrategy, requestLimit, period);
        ProxyManager<String> proxyManager = lettuceBasedProxyManager;

        return proxyManager.builder().build(id, configurationLazySupplier);
    }
}
