package com.loopin.loopinbackend.domain.user.service;

import com.loopin.loopinbackend.domain.user.dto.request.UserProfileUpdateRequest;
import com.loopin.loopinbackend.domain.user.dto.request.UserRegisterRequest;
import com.loopin.loopinbackend.domain.user.dto.response.UserInfoResponse;

public interface UserService {
    String register(UserRegisterRequest request);

    UserInfoResponse getMyInfo();

    void updatePassword(String oldPassword, String newPassword);

    void updateProfile(UserProfileUpdateRequest request);

    void deleteUser(String password);

    UserInfoResponse getUserInfo(Long userId);

    boolean checkEmail(String email);

    boolean checkNickname(String nickname);
}
