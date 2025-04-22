package org.example.user.application.port;

import java.util.UUID;

import org.example.user.application.dto.OAuth2UserInfo;
import org.example.user.application.dto.UserDto;

public interface UserReader {

    UserDto loadUser(OAuth2UserInfo oAuth2UserInfo);

    UserDto loadUser(UUID userId);

}
