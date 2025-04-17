package org.example.user.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.common.exception.ErrorCode;
import org.example.user.adapter.out.UserApiKeyRepository;
import org.example.user.domain.entity.UserApiKey;
import org.example.user.domain.exception.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserApiKeyService {

    private final UserApiKeyRepository userApiKeyRepository;

    // ==== 초기 생성 ====
    @Transactional
    public UserApiKey createUserApiKey(UUID userId) {
        validateUserId(userId);

        return userApiKeyRepository.findByUserId(userId)
                .orElseGet(() -> userApiKeyRepository.save(UserApiKey.create(userId)));
    }

    // ==== 조회 ====
    @Transactional(readOnly = true)
    public String getValidApiKey(UUID userId) {
        validateUserId(userId);

        UserApiKey userApiKey = findByUserIdOrThrow(userId);

        if (userApiKey.isExpired()) {
            throw new UserException(ErrorCode.USER_API_KEY_EXPIRED, userId);
        }

        return userApiKey.getApiKey();
    }

    // ==== 갱신 ====
    @Transactional
    public String updateApiKey(UUID userId, String newApiKey, int expireDays) {
        validateUserId(userId);

        UserApiKey apiKey = findByUserIdOrThrow(userId);

        LocalDateTime newExpiresAt = LocalDateTime.now().plusDays(expireDays);
        apiKey.regenerateApiKey(newApiKey, newExpiresAt);

        userApiKeyRepository.save(apiKey);

        return apiKey.getApiKey();
    }

    @Transactional
    public void deleteApiKey(UUID userId) {
        validateUserId(userId);

        UserApiKey apiKey = findByUserIdOrThrow(userId);
        apiKey.deleteKey();

        userApiKeyRepository.save(apiKey);
    }

    // ===== 공통 유틸 =====
    private UserApiKey findByUserIdOrThrow(UUID userId) {
        return userApiKeyRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_API_KEY_NOT_FOUND, userId));
    }

    private void validateUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
    }

}
