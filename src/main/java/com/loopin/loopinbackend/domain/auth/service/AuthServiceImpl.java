package com.loopin.loopinbackend.domain.auth.service;


import com.loopin.loopinbackend.domain.auth.dto.LoginResponse;
import com.loopin.loopinbackend.domain.auth.dto.TokensDto;
import com.loopin.loopinbackend.domain.auth.dto.request.UserLoginRequest;
import com.loopin.loopinbackend.domain.auth.dto.response.UserLoginResponse;
import com.loopin.loopinbackend.domain.auth.exception.BlacklistTokenException;
import com.loopin.loopinbackend.domain.auth.exception.InvalidJwtException;
import com.loopin.loopinbackend.domain.auth.exception.InvalidLoginException;
import com.loopin.loopinbackend.domain.auth.model.CustomUserDetails;
import com.loopin.loopinbackend.domain.user.entity.User;
import com.loopin.loopinbackend.domain.user.exception.UserNotFoundException;
import com.loopin.loopinbackend.domain.user.repository.UserJpaRepository;
import com.loopin.loopinbackend.global.security.jwt.provider.JwtProvider;
import com.loopin.loopinbackend.global.security.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final UserJpaRepository userJpaRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     *
     * @param request 이메일, 패스워드
     * @return 액세스, 리프레시 토큰
     */
    public LoginResponse login(UserLoginRequest request) {
        log.debug("Login Request: {}", request);
        String email = request.getEmail();
        String password = request.getPassword();

        try {
            // login 진행
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
            String userId = principal.getUserId().toString();

            String accessToken = jwtProvider.generateAccessToken(auth);
            String refreshToken = jwtProvider.generateRefreshToken(auth);

            String redisKey = "refresh:" + userId;
            long ttl = jwtProvider.extractExpiration(refreshToken) - System.currentTimeMillis();

            redisTemplate.opsForValue().set(redisKey, refreshToken, Duration.ofMillis(ttl));

            User loggedInUser = principal.user();

            return new LoginResponse(refreshToken, UserLoginResponse.of(loggedInUser, accessToken));
        } catch (AuthenticationException ex) {
            System.out.println(ex.getMessage());
            throw new InvalidLoginException();
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        String accessToken = SecurityUtils.resolveToken(request);
        if (accessToken == null) return;

        String userId = jwtProvider.extractUserId(accessToken).toString();
        long ttl = jwtProvider.extractExpiration(accessToken) - System.currentTimeMillis();

        // 토큰이 유효하면 엑세스 토큰 블랙리스트 등록
        if (ttl > 0) {
            String blackKey = "blacklist:" + accessToken;
            redisTemplate.opsForValue().set(blackKey, "logout", Duration.ofMillis(ttl));
        }

        // 리프레시 토큰 레디스에서 삭제
        redisTemplate.delete("refresh:" + userId);
    }

    @Override
    public TokensDto refreshToken(String refreshToken) {
        System.out.println("refreshToken = " + refreshToken);

        // 리프레시 토큰 검증
        if (redisTemplate.hasKey("BL:RT:" + refreshToken)) throw new BlacklistTokenException();
        if (!jwtProvider.validateToken(refreshToken)) throw new InvalidJwtException();

        // 값 추출
        String userId = jwtProvider.extractUserId(refreshToken).toString();
        String username = jwtProvider.extractUsername(refreshToken);
        String storedValue = redisTemplate.opsForValue().get("refresh:" + userId);

        System.out.println("storedValue = " + storedValue);
        System.out.println("stored = " + refreshToken);

        // 리프레시 토큰과 레디스 키값이 같지 않다면
        if (storedValue == null || !storedValue.equals(refreshToken)) throw new InvalidJwtException();

        // 새로운 토큰 발행
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String newAccessToken = jwtProvider.generateAccessToken(auth);
        String newRefreshToken = jwtProvider.generateRefreshToken(auth);
        long blTtl = jwtProvider.extractExpiration(refreshToken) - System.currentTimeMillis();
        long ttl = jwtProvider.extractExpiration(newRefreshToken) - System.currentTimeMillis();

        // 사용한 리프레시 토큰 블랙리스트 등록
        redisTemplate.opsForValue().set("BL:RT:" + refreshToken, "used", Duration.ofMillis(blTtl));
        // 새로운 리프레시 토큰 등록
        redisTemplate.opsForValue().set("refresh:" + userId, newRefreshToken, Duration.ofMillis(ttl));

        System.out.println("newRefreshToken = " + newRefreshToken);

        return TokensDto.builder()
                        .accessToken(newAccessToken)
                                .refreshToken(newRefreshToken)
                                        .build();
    }

    @Override
    public UserLoginResponse me(Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return UserLoginResponse.builder()
                .accessToken(null)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .userId(user.getId())
                .role(user.getRole().name())
                .build();
    }


}
