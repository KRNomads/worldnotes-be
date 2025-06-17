package org.example.user.application.dto;

import java.util.UUID;

import org.example.common.enums.SocialProvider;
import org.example.user.domain.entity.User;
import org.example.user.domain.enums.Role;

public record UserDto(
        UUID userId,
        Role role,
        SocialProvider provider,
        String providerId,
        String email,
        String name,
        String profileImg,
        Boolean isDeleted) {

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getRole(),
                user.getProvider(),
                user.getProviderId(),
                user.getEmail(),
                user.getName(),
                user.getProfileImg(), // 이 필드가 User에 있다고 가정
                user.getIsDeleted()
        );
    }
}
