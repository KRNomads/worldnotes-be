package org.example.auth.domain.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ApiKey {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, length = 64)
    private String key;

    private UUID userId;

    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    // === 비즈니스 로직 ===
    @Builder
    public ApiKey(String key, UUID userId, LocalDateTime expiresAt) {
        this.key = key;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    // API 키 비활성화
    public void disable() {
        this.enabled = false;
    }

    // API 키 재활성화
    public void enable() {
        this.enabled = true;
    }

    // API 키 유효성 검사
    public boolean isValid() {
        return enabled && LocalDateTime.now().isBefore(expiresAt);
    }

}
