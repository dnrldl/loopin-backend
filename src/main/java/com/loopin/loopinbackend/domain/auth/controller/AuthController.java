package com.loopin.loopinbackend.domain.auth.controller;

import com.loopin.loopinbackend.domain.auth.dto.LoginResponse;
import com.loopin.loopinbackend.domain.auth.dto.TokensDto;
import com.loopin.loopinbackend.domain.auth.dto.request.UserLoginRequest;
import com.loopin.loopinbackend.domain.auth.dto.response.UserLoginResponse;
import com.loopin.loopinbackend.domain.auth.exception.InvalidJwtException;
import com.loopin.loopinbackend.domain.auth.exception.UnauthorizedAccessException;
import com.loopin.loopinbackend.domain.auth.service.AuthServiceImpl;
import com.loopin.loopinbackend.domain.user.exception.UserNotFoundException;
import com.loopin.loopinbackend.global.response.ApiErrorResponse;
import com.loopin.loopinbackend.global.response.ApiSuccessResponse;
import com.loopin.loopinbackend.global.security.annotation.AuthUserId;
import com.loopin.loopinbackend.global.security.annotation.PublicApi;
import com.loopin.loopinbackend.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    @Operation(summary = "로그인",
            description = "사용자가 이메일, 비밀번호를 입력하여 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<UserLoginResponse>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "로그인 요청 DTO", required = true)
            @RequestBody UserLoginRequest userLoginRequest,
            HttpServletResponse response) {
        LoginResponse result = authService.login(userLoginRequest);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", result.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ApiSuccessResponse.of(result.getUserLoginResponse()));
    }

    @Operation(summary = "엑세스 토큰 재발급",
            description = "사용자의 리프레시 토큰으로 액세스 토큰을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "토큰 재발급 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiSuccessResponse<String>> refresh(@CookieValue(value = "refreshToken", defaultValue = "") String refreshToken, HttpServletResponse response) {
        System.out.println("refreshToken = " + refreshToken);
        if (refreshToken.isBlank()) throw new IllegalArgumentException();
        TokensDto tokens = authService.refreshToken(refreshToken);

        // 클라이언트에 쿠키 응답(저장)
        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ApiSuccessResponse.of(tokens.getAccessToken()));
    }

    // private
    @Operation(summary = "로그아웃",
            description = "사용자의 액세스 토큰으로 로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiSuccessResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request);

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(0) // 쿠키 삭제
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary = "내 정보 조회(갱신)",
            description = "사용자의 액세스 토큰으로 내 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @GetMapping("/me")
    public ResponseEntity<ApiSuccessResponse<UserLoginResponse>> me(@AuthUserId Long userId) {
        if (userId == null) throw new UnauthorizedAccessException();
        return ResponseEntity.ok(ApiSuccessResponse.of(authService.me(userId)));
    }
}
