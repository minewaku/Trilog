package com.minewaku.trilog.service;

import java.util.function.Supplier;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import jakarta.servlet.http.HttpServletRequest;

public interface IRateLimitService {
	Supplier<BucketConfiguration> bucketConfiguration(String limitLevel);
	Supplier<BucketConfiguration> bucketConfiguration(String limitStrategy, int requestLimit, int period);
	
	Bucket redisBucket(HttpServletRequest request, String limitLevel);
	Bucket redisBucket(HttpServletRequest request, String limitStrategy, int requestLimit, int period);
	Bucket redisBucket(String id, String limitLevel);
	Bucket redisBucket(String id, String limitStrategy, int requestLimit, int period);
}
