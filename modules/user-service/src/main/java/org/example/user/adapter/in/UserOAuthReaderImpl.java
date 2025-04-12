package org.example.user.adapter.in;

import org.example.user.application.UserRegistrationService;
import org.example.user.application.dto.OAuth2UserInfo;
import org.example.user.application.dto.UserDto;
import org.example.user.application.port.UserOAuthReader;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserOAuthReaderImpl implements UserOAuthReader {

    private final UserRegistrationService userRegistrationService;

    @Override
    public UserDto loadUser(OAuth2UserInfo oAuth2UserInfo) {
        return userRegistrationService.loadUser(oAuth2UserInfo);
    }

}
