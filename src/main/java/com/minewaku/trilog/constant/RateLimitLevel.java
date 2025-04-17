package com.minewaku.trilog.constant;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;



public enum RateLimitLevel {

    UNLIMITED {
        public Bandwidth getLimit() {
            return Bandwidth.builder().capacity(Long.MAX_VALUE).refillIntervally(Long.MAX_VALUE, Duration.ofDays(365)).build();
        }
    },

    HIGH {
        public Bandwidth getLimit() {
            return Bandwidth.builder().capacity(1000).refillIntervally(1000, Duration.ofMinutes(1)).build();
        }
    },

    MEDIUM {
        public Bandwidth getLimit() {
            return Bandwidth.builder().capacity(10).refillIntervally(100, Duration.ofMinutes(0)).build();
        }
    },

    LOW {
        public Bandwidth getLimit() {
            return Bandwidth.builder().capacity(1).refillIntervally(1, Duration.ofMinutes(1)).build();
        }
    };

    public abstract Bandwidth getLimit();

}
