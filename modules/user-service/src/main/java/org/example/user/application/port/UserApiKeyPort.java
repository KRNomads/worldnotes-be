package org.example.user.application.port;

import java.util.UUID;

public interface UserApiKeyPort {

    String updateApiKey(UUID userId, String newApiKey, int expireDays);

    void deleteApiKey(UUID userId);

    String getValidApiKey(UUID userId);

}
