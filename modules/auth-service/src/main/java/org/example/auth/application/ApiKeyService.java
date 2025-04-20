package org.example.auth.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.auth.adapter.out.ApiKeyJpaRepository;
import org.example.auth.domain.entity.ApiKey;
import org.example.auth.domain.exception.ApiKeyException;
import org.example.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiKeyService {

    private final ApiKeyJpaRepository apiKeyJpaRepository;

    private static final int API_KEY_EXPIRE_DAYS = 3;

    // === 발급 ===
    @Transactional
    public String issueApiKey(UUID userId) {
        String newKey = generateApiKey();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(API_KEY_EXPIRE_DAYS);

        ApiKey apiKey = ApiKey.builder()
                .key(newKey)
                .userId(userId)
                .expiresAt(expiresAt)
                .build();

        ApiKey savedApiKey = apiKeyJpaRepository.save(apiKey);

        return savedApiKey.getKey();
    }

    // === 검증 ===
    @Transactional(readOnly = true)
    public UUID validateApiKey(String apiKey) {
        ApiKey apiKeyEntity = apiKeyJpaRepository.findByKey(apiKey)
                .orElseThrow(() -> new ApiKeyException(ErrorCode.API_KEY_NOT_FOUND, apiKey));

        if (!apiKeyEntity.isValid()) {
            throw new ApiKeyException(ErrorCode.API_KEY_INVALID, apiKey);
        }

        return apiKeyEntity.getUserId();
    }

    // === 유틸 함수 ===
    private String generateApiKey() {
        return UUID.randomUUID().toString().replace("-", ""); // 더 강한 보안이 필요하면 SHA 기반도 가능
    }

}
