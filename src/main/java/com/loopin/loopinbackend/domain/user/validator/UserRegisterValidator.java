package com.loopin.loopinbackend.domain.user.validator;

import com.loopin.loopinbackend.domain.user.dto.request.UserRegisterRequest;
import com.loopin.loopinbackend.domain.user.exception.*;
import com.loopin.loopinbackend.domain.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterValidator {
    private final UserJpaRepository userJpaRepository;

    public void validate(UserRegisterRequest request) {
        if (userJpaRepository.existsByEmail(request.getEmail())) throw new DuplicateEmailException();
        if (userJpaRepository.existsByNickname(request.getNickname())) throw new DuplicateNicknameException();
        if (userJpaRepository.existsByPhoneNumber(request.getPhoneNumber())) throw new DuplicatePhoneNumberException();
    }
}
