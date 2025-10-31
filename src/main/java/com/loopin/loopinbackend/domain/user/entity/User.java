package com.loopin.loopinbackend.domain.user.entity;

import com.loopin.loopinbackend.domain.user.enums.Gender;
import com.loopin.loopinbackend.domain.user.enums.Provider;
import com.loopin.loopinbackend.domain.user.enums.Role;
import com.loopin.loopinbackend.domain.user.enums.Status;
import com.loopin.loopinbackend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // postgresql 에서는 "user"가 예약어라서 "users" 로 대체
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String firstName;

    @Column(nullable = false, length = 30)
    private String lastName;

    @Column(nullable = false, unique = true, length = 60)
    private String nickname;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profileImageUrl;

    @Column(length = 300)
    private String bio; // 자기소개

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    private String providerId;

    private LocalDateTime lastLoginAt;

    @Column(nullable = false)
    private LocalDate birth;

    @Builder
    public User(String email, String password, String firstName, String lastName,
                String nickname, String phoneNumber, Gender gender, String profileImageUrl,
                String bio, Role role, Status status, Boolean emailVerified,
                Provider provider, String providerId, LocalDateTime lastLoginAt, LocalDate birth) {

        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.role = role != null ? role : Role.USER;
        this.status = status != null ? status : Status.ACTIVE;
        this.emailVerified = emailVerified != null ? emailVerified : false;
        this.provider = provider != null ? provider : Provider.LOCAL;
        this.providerId = providerId;
        this.lastLoginAt = lastLoginAt;
        this.birth = birth;
    }
}
