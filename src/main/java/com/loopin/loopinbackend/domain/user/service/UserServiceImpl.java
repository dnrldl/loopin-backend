package com.loopin.loopinbackend.domain.user.service;

import com.loopin.loopinbackend.global.security.util.SecurityUtils;
import com.loopin.loopinbackend.domain.user.dto.request.UserProfileUpdateRequest;
import com.loopin.loopinbackend.domain.user.dto.request.UserRegisterRequest;
import com.loopin.loopinbackend.domain.user.dto.response.UserInfoResponse;
import com.loopin.loopinbackend.domain.user.entity.User;
import com.loopin.loopinbackend.domain.user.enums.Provider;
import com.loopin.loopinbackend.domain.user.enums.Role;
import com.loopin.loopinbackend.domain.user.enums.Status;
import com.loopin.loopinbackend.domain.user.exception.InvalidPasswordException;
import com.loopin.loopinbackend.domain.user.exception.UserNotFoundException;
import com.loopin.loopinbackend.domain.user.repository.UserJpaRepository;
import com.loopin.loopinbackend.domain.user.validator.UserPasswordUpdateValidator;
import com.loopin.loopinbackend.domain.user.validator.UserRegisterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRegisterValidator userRegisterValidator;
    private final UserPasswordUpdateValidator userPasswordUpdateValidator;

    @Override
    public String register(UserRegisterRequest request) {
        userRegisterValidator.validate(request);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .birth(request.getBirth())
                .role(Role.USER)
                .status(Status.ACTIVE)
                .provider(Provider.LOCAL)
                .emailVerified(false)
                .build();

        User savedUser = userJpaRepository.save(user);
        return savedUser.getEmail();
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        User currentUser = SecurityUtils.getCurrentUser();
        userPasswordUpdateValidator.validate(passwordEncoder, currentUser, oldPassword, newPassword);

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userJpaRepository.save(currentUser);
    }

    @Override
    public void updateProfile(UserProfileUpdateRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

        currentUser.setNickname(request.getNickname());
        currentUser.setProfileImageUrl(request.getProfileImageUrl());
        currentUser.setBio(request.getBio());

        userJpaRepository.save(currentUser);
    }

    @Override
    public void deleteUser(String password) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (!passwordEncoder.matches(password, currentUser.getPassword())) throw new InvalidPasswordException();

        userJpaRepository.delete(currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo() {
        User currentUser = SecurityUtils.getCurrentUser();
        return UserInfoResponse.of(currentUser);
    }

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserInfoResponse.of(user);
    }

    @Override
    public boolean checkEmail(String email) {
        return !userJpaRepository.existsByEmail(email.trim().toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean checkNickname(String nickname) {
        return !userJpaRepository.existsByNickname(nickname.trim());
    }
}
