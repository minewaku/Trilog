package com.minewaku.trilog.constant;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;

public enum RateLimitStrategy {
    RATE_LIMIT_IN_SECONDS {
        public Bandwidth getLimit(int requestLimit, int periodInSeconds) {
            return Bandwidth.builder().capacity(requestLimit).refillIntervally(requestLimit, Duration.ofSeconds(periodInSeconds)).initialTokens(requestLimit).build();
        }
    },

    RATE_LIMIT_IN_MINUTES {
        public Bandwidth getLimit(int requestLimit, int periodInMinutes) {
            return Bandwidth.builder().capacity(requestLimit).refillIntervally(requestLimit, Duration.ofMinutes(periodInMinutes)).initialTokens(requestLimit).build();
        }
    },

    RATE_LIMIT_IN_HOURS {
        public Bandwidth getLimit(int requestLimit, int periodInHours) {
            return Bandwidth.builder().capacity(requestLimit).refillIntervally(requestLimit, Duration.ofHours(periodInHours)).initialTokens(requestLimit).build();
        }
    },
    
    RATE_LIMIT_IN_DAYS {
        public Bandwidth getLimit(int requestLimit, int periodInDays) {
            return Bandwidth.builder().capacity(requestLimit).refillIntervally(requestLimit, Duration.ofDays(periodInDays)).initialTokens(requestLimit).build();
        }
    };

    public abstract Bandwidth getLimit(int requests, int period);
}
