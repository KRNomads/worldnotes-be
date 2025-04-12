package org.example.user.application.port;

import org.example.user.application.dto.OAuth2UserInfo;
import org.example.user.application.dto.UserDto;

public interface UserOAuthReader {

    UserDto loadUser(OAuth2UserInfo oAuth2UserInfo);

}
