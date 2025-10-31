package com.loopin.loopinbackend.global.security.config;

import com.loopin.loopinbackend.global.security.entrypoint.CustomAuthenticationEntryPoint;
import com.loopin.loopinbackend.global.security.handler.CustomAccessDeniedHandler;
import com.loopin.loopinbackend.global.security.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String[] permitAllPatterns = {"/api/pub/**", "/swagger-ui/**", "/v3/api-docs/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/api/auth/**").permitAll()

                                    // 중복 체크 허용
                                    .requestMatchers("/api/users/check/**").permitAll()

                                    // Swagger 문서 허용
                                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                                    // 회원가입 허용
                                    .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()

                                    // 유저 조회 허용
                                    .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()

                                    // 게시글 조회 허용
                                    .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()

                                    .requestMatchers(HttpMethod.POST, "/api/posts").authenticated()

                                    // 댓글 조회 허용
                                    .requestMatchers(HttpMethod.GET, "/api/posts/*/comments").permitAll();
                        }
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint) // 401
                        .accessDeniedHandler(accessDeniedHandler) // 403
                );

        // oAuth2

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


