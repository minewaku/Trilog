package com.minewaku.trilog.config.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.minewaku.trilog.service.impl.RateLimitService;
import com.minewaku.trilog.util.LogUtil;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestThrottleFilter extends OncePerRequestFilter {

    private int MAX_REQUESTS = 1000;

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Bucket bucket = rateLimitService.redisBucket(request, "RATE_LIMIT_IN_SECONDS", MAX_REQUESTS, 1);
        Bucket bucket = rateLimitService.redisBucket(request, "HIGH");
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        LogUtil.LOGGER.info("Remain Tokens: " + probe.getRemainingTokens());
        if(!probe.isConsumed()){
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }

        filterChain.doFilter(request, response);
    }

}
