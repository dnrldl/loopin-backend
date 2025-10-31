package com.loopin.loopinbackend.domain.user.validator;

import com.loopin.loopinbackend.domain.user.entity.User;
import com.loopin.loopinbackend.domain.user.exception.InvalidPasswordException;
import com.loopin.loopinbackend.domain.user.exception.SamePasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordUpdateValidator {
    public void validate(PasswordEncoder passwordEncoder, User currentUser, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) throw new InvalidPasswordException();
        if (passwordEncoder.matches(newPassword, currentUser.getPassword())) throw new SamePasswordException();
    }
}
