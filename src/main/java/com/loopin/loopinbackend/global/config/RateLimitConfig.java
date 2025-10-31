package com.loopin.loopinbackend.global.config;

import com.amazonaws.services.s3.model.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {
//    @Bean
//    public Bucket uploadPresignBucket() {
//        // 예: 분당 60회
//        Bandwidth limit = Bandwidth.classic(
//                60,                      // capacity
//                Refill.greedy(60, Duration.ofMinutes(1)) // refill 60 tokens every 1 minute
//        );
//        return Bucket.builder().addLimit(limit).build();
//    }
}
