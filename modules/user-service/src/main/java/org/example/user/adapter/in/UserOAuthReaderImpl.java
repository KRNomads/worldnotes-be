package org.example.user.adapter.in;

import java.util.UUID;

import org.example.user.application.UserRegistrationService;
import org.example.user.application.UserService;
import org.example.user.application.dto.OAuth2UserInfo;
import org.example.user.application.dto.UserDto;
import org.example.user.application.port.UserReader;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserOAuthReaderImpl implements UserReader {

    private final UserRegistrationService userRegistrationService;
    private final UserService userService;

    @Override
    public UserDto loadUser(OAuth2UserInfo oAuth2UserInfo) {
        return userRegistrationService.loadUser(oAuth2UserInfo);
    }

    @Override
    public UserDto loadUser(UUID userId) {
        return userService.getUserById(userId);
    }

}
