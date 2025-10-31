package com.loopin.loopinbackend.domain.user.controller;

import com.loopin.loopinbackend.domain.user.dto.request.UserDeleteRequest;
import com.loopin.loopinbackend.domain.user.dto.request.UserPasswordUpdateRequest;
import com.loopin.loopinbackend.domain.user.dto.request.UserProfileUpdateRequest;
import com.loopin.loopinbackend.domain.user.dto.request.UserRegisterRequest;
import com.loopin.loopinbackend.domain.user.dto.response.UserInfoResponse;
import com.loopin.loopinbackend.domain.user.service.UserService;
import com.loopin.loopinbackend.global.response.ApiErrorResponse;
import com.loopin.loopinbackend.global.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // public
    @Operation(summary = "회원가입",
            description = "사용자가 이메일, 비밀번호 등 정보를 입력하여 회원가입합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 입력값", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse<String>> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "회원가입 요청 DTO", required = true)
            @RequestBody UserRegisterRequest request
    ) {
        System.out.println("request.toString() = " + request.toString());
        String response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiSuccessResponse.of(response));
    }

    @Operation(summary = "이메일 중복 체크",
            description = "등록된 이메일인지 체크")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @GetMapping("/check/email")
    public ResponseEntity<ApiSuccessResponse<Boolean>> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(ApiSuccessResponse.of(userService.checkEmail(email)));
    }

    @Operation(summary = "닉네임 중복 체크",
            description = "등록된 닉네임인지 체크")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @GetMapping("/check/nickname")
    public ResponseEntity<ApiSuccessResponse<Boolean>> checkNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(ApiSuccessResponse.of(userService.checkNickname(nickname)));
    }

    // private
    @Operation(summary = "유저 정보 조회",
            description = "유저 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<ApiSuccessResponse<UserInfoResponse>> getUserInfo(@PathVariable Long userId) {
        UserInfoResponse res = userService.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.of(res));
    }

    // private
    @Operation(summary = "내 정보 조회",
            description = "내 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<ApiSuccessResponse<UserInfoResponse>> getMyInfo() {
        UserInfoResponse res = userService.getMyInfo();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.of(res));
    }

    @Operation(summary = "내 비밀번호 변경",
            description = "현재 로그인된 사용자의 비밀번호를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/password")
    public ResponseEntity<ApiSuccessResponse<Void>> updatePassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "유저 암호 변경 요청 DTO", required = true)
            @Valid @RequestBody UserPasswordUpdateRequest request
    ) {
        userService.updatePassword(request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok(ApiSuccessResponse.of(null));
    }

    @Operation(summary = "내 프로필 변경",
            description = "현재 로그인된 사용자의 프로필을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 변경 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/profile")
    public ResponseEntity<ApiSuccessResponse<Void>> updateProfile(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "유저 정보 변경 요청 DTO", required = true)
            @Valid @RequestBody UserProfileUpdateRequest request
    ) {
        userService.updateProfile(request);
        return ResponseEntity.ok(ApiSuccessResponse.of(null));
    }

    @Operation(summary = "회원 탈퇴",
            description = "현재 로그인된 사용자를 회원 탈퇴합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<ApiSuccessResponse<Void>> deleteUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "유저 탈퇴 요청 DTO", required = true)
            @Valid @RequestBody UserDeleteRequest request
            ) {
        userService.deleteUser(request.getPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiSuccessResponse.of(null));
    }
}
