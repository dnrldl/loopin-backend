package com.loopin.loopinbackend.global.security.util;

import com.loopin.loopinbackend.domain.auth.exception.UnauthorizedAccessException;
import com.loopin.loopinbackend.domain.auth.model.CustomUserDetails;
import com.loopin.loopinbackend.domain.user.entity.User;
import com.loopin.loopinbackend.global.security.annotation.AuthUserId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class SecurityUtils {

    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedAccessException();
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails(User user)) {
            return user;
        }

        throw new UnauthorizedAccessException();
    }

    public static boolean isAuthenticated() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth != null && auth.isAuthenticated();
        } catch (Exception e) {
            log.warn("Authenticated failed", e);
            return false;
        }
    }

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public static String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }

    public static Long resolve(AuthUserId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // AnonymousAuthenticationToken도 차단 (isAuthenticated() 가 true일 수 있음)
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return handleNoAuth(id);
        }

        Object principal = auth.getPrincipal();

        // 1) CustomUserDetails
        if (principal instanceof CustomUserDetails cud) {
            return cud.getUserId();
        }

        // 2) 일반 UserDetails (username에 숫자 id를 넣어둔 경우만)
        if (principal instanceof UserDetails ud) {
            Long parsed = tryParseLong(ud.getUsername());
            return parsed != null ? parsed : handleNoUserId(id);
        }

        // 3) 문자열 principal (익명/username 문자열 등)
        if (principal instanceof String s) {
            // "anonymousUser" 차단
            if ("anonymousUser".equalsIgnoreCase(s)) {
                return handleNoAuth(id);
            }
            Long parsed = tryParseLong(s);
            return parsed != null ? parsed : handleNoUserId(id);
        }

        // 4) 기타 타입
        return handleNoUserId(id);
    }

    private static Long tryParseLong(String s) {
        try { return Long.valueOf(s); } catch (Exception e) { return null; }
    }


    private static Long handleNoAuth(AuthUserId id) {
        if (!id.required()) {
            return id.defaultValue() == 0 ? null : id.defaultValue();
        }
        return null;
    }

    private static Long handleNoUserId(AuthUserId id) {
        if (!id.required()) {
            return id.defaultValue() == 0 ? null : id.defaultValue();
        }
        return null;
    }
}
