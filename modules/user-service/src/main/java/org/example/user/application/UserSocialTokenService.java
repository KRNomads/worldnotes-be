package org.example.user.application;

import java.util.UUID;

import org.example.common.exception.ErrorCode;
import org.example.user.adapter.out.UsersSocialTokenRepository;
import org.example.user.domain.entity.UserSocialToken;
import org.example.user.domain.exception.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSocialTokenService {

    private final UsersSocialTokenRepository usersSocialTokenRepository;

    // ==== 초기 생성 ====
    @Transactional
    public void createUserSocialToken(UUID userId) {
        validateUserId(userId);

        usersSocialTokenRepository.findByUserId(userId)
                .orElseGet(() -> usersSocialTokenRepository.save(UserSocialToken.create(userId)));
    }

    // ==== 조회 ====
    @Transactional(readOnly = true)
    public String getSocialRefreshToken(UUID userId) {
        validateUserId(userId);

        return findByUserIdOrThrow(userId).getRefreshToken();
    }

    // ==== 갱신 ====
    @Transactional
    public void updateRefreshToken(UUID userId, String newRefreshToken) {
        validateUserId(userId);

        UserSocialToken token = findByUserIdOrThrow(userId);
        token.setRefreshToken(newRefreshToken);

        usersSocialTokenRepository.save(token);
    }

    @Transactional
    public void deleteRefreshToken(UUID userId) {
        validateUserId(userId);

        UserSocialToken token = findByUserIdOrThrow(userId);
        token.setRefreshToken(null);

        usersSocialTokenRepository.save(token);
    }

    // ===== 공통 유틸 =====
    private UserSocialToken findByUserIdOrThrow(UUID userId) {
        return usersSocialTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_SOCIAL_TOKEN_NOT_FOUND, userId));
    }

    private void validateUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
    }
}
