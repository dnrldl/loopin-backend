package com.loopin.loopinbackend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;


import java.util.List;

/**
 * WebMvcConfigurer
 * Spring MVC 레벨에서 적용
 * DispatcherServlet에 들어오는 요청만 허용
 * 즉, 시큐리티와 별도로 동작하여 403 오류가 발생할 수 있음
 *
 * orsConfigurationSource + Security Filter
 * Spring Security FilterChain 레벨에서 적용
 */
@Configuration
public class CorsConfig {

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowedOrigins(List.of("*")); // or specific domains
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
////        config.setExposedHeaders(List.of("Authorization", "Refresh-Token")); // 필요한 경우
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
}
