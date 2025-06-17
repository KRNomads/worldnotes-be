package org.example.auth.adapter.in.response;

import java.util.UUID;

import org.example.user.domain.enums.Role;

public record UserInfoResponse(
        UUID userId,
        String email,
        String name,
        Role role,
        String profileImg
        ) {

}
