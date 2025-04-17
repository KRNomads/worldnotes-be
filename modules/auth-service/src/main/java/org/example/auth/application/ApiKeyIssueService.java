package org.example.auth.application;

import java.util.UUID;

import org.example.user.application.port.UserApiKeyPort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApiKeyIssueService {

    private final UserApiKeyPort userApiKeyPort;
    private static final int API_KEY_EXPIRE_DAYS = 3;

    public String issueApiKey(UUID userId) {
        String newApiKey = generateApiKey();

        String apiKey = userApiKeyPort.updateApiKey(userId, newApiKey, API_KEY_EXPIRE_DAYS);

        return apiKey;
    }

    private String generateApiKey() {
        return UUID.randomUUID().toString().replace("-", ""); // 더 강한 보안이 필요하면 SHA 기반도 가능
    }

}
