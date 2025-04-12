package org.example.user.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private Long tokenId;

    private UUID userId;

    private String refreshToken;

    private LocalDateTime updatedAt;

    // 생성자
    public UserSocialToken(Long tokenId, UUID userId, String refreshToken, LocalDateTime updatedAt) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.updatedAt = updatedAt;
    }

    // 정적 팩토리 메서드
    public static UserSocialToken create(UUID userId) {
        return new UserSocialToken(
                null,
                userId,
                null,
                LocalDateTime.now());
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.updatedAt = LocalDateTime.now();
    }

}
