package org.example.user.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class UserSocialToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    private String refreshToken;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // === 비즈니스 로직 ===
    public UserSocialToken(Long tokenId, UUID userId, String refreshToken, LocalDateTime updatedAt) {
        this.id = tokenId;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.updatedAt = updatedAt;
    }

    public static UserSocialToken create(UUID userId) {
        return new UserSocialToken(
                null,
                userId,
                null,
                LocalDateTime.now());
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
