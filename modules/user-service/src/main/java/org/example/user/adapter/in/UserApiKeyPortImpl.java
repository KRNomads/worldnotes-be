package org.example.user.adapter.in;

import java.util.UUID;

import org.example.user.application.UserApiKeyService;
import org.example.user.application.port.UserApiKeyPort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserApiKeyPortImpl implements UserApiKeyPort {

    private final UserApiKeyService userApiKeyService;

    @Override
    public String updateApiKey(UUID userId, String newApiKey, int expireDays) {
        return userApiKeyService.updateApiKey(userId, newApiKey, expireDays);
    }

    @Override
    public void deleteApiKey(UUID userId) {
        userApiKeyService.deleteApiKey(userId);
    }

    @Override
    public String getValidApiKey(UUID userId) {
        return userApiKeyService.getValidApiKey(userId);
    }

}
