package org.example.user.application.dto;

import java.util.UUID;

import org.example.common.enums.SocialProvider;
import org.example.user.domain.enums.Role;

public record UserDto(
        UUID userId,
        Role role,
        SocialProvider provider,
        String providerId,
        String email,
        String name,
        Boolean isDeleted) {

}
