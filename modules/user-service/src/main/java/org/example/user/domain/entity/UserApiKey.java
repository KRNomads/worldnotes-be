package org.example.user.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class UserApiKey {

    @Id
    @GeneratedValue
    private Long id;

    private UUID userId;

    @Column(unique = true, length = 64)
    private String apiKey;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime expiresAt;

    // === 비즈니스 로직 ===
    public UserApiKey(UUID userId) {
        this.userId = userId;
    }

    public static UserApiKey create(UUID userId) {
        return new UserApiKey(userId);
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    public void regenerateApiKey(String newKey, LocalDateTime newExpiresAt) {
        this.apiKey = newKey;
        this.expiresAt = newExpiresAt;
    }

    public void deleteKey() {
        this.apiKey = null;
        this.expiresAt = null;
    }

}
