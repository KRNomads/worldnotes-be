package org.example.user.application.dto;

import java.util.UUID;

import org.example.common.enums.SocialProvider;

public record UserDto(
        UUID userId,
        String role,
        SocialProvider provider,
        String providerId,
        String email,
        String name,
        Boolean isDeleted) {

}
