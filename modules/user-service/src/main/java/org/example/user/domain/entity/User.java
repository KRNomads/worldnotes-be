package org.example.user.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.common.enums.SocialProvider;
import org.example.user.domain.enums.Role;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    private String providerId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    // === 비즈니스 로직 ===
    @Builder
    public User(UUID id, Role role, String name, String email, SocialProvider provider,
            String providerId, LocalDateTime createdAt, Boolean isDeleted, LocalDateTime deletedAt) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    public static User create(String name, String email, SocialProvider provider, String providerId) {
        return User.builder()
                .id(UUID.randomUUID())
                .name(name)
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .deletedAt(null)
                .build();
    }

    public void updateNameAndEmail(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // 소프트 삭제
    public void deleteUser() {
        if (Boolean.TRUE.equals(this.isDeleted)) {
            throw new IllegalStateException("이미 삭제된 유저입니다.");
        }
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    // 복구
    public void recoveryUser() {
        if (!Boolean.TRUE.equals(this.isDeleted)) {
            throw new IllegalStateException("삭제되지 않은 유저는 복구할 수 없습니다.");
        }
        this.isDeleted = false;
        this.deletedAt = null;
    }

    // 하드 삭제 ( 복구 불가 )
    public void hardDeleteUser() {
        this.name = null;
        this.email = null;
        this.provider = null;
        this.providerId = null;
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}
