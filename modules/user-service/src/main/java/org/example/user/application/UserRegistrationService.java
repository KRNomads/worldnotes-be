package org.example.user.application;

import java.util.UUID;

import org.example.common.enums.SocialProvider;
import org.example.user.application.dto.OAuth2UserInfo;
import org.example.user.application.dto.UserDto;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserService userService;
    private final UserSocialTokenService userSocialTokenService;
    private final UserApiKeyService userApiKeyService;

    // 유저 정보 로드 
    public UserDto loadUser(OAuth2UserInfo oAuth2UserInfo) {
        SocialProvider provider = oAuth2UserInfo.provider();
        String providerId = oAuth2UserInfo.providerId();
        String name = oAuth2UserInfo.name();
        String email = oAuth2UserInfo.email();

        // 유저가 존재할 경우 로드, 없으면 회원가입
        UserDto userDto = userService.getUserByProvider(provider, providerId);
        userDto = userDto != null ? userDto : registUser(oAuth2UserInfo);

        // 유저 이메일, 이름 검증 후 다를 시 업데이트
        if (!userDto.name().equals(name) || !userDto.email().equals(email)) {
            userDto = userService.updateUserNameAndEmail(userDto.userId(), name, email);
        }

        return userDto;
    }

    // 회원가입
    @Transactional
    private UserDto registUser(OAuth2UserInfo oAuth2UserInfo) {
        UserDto userDTO = userService.createUser(oAuth2UserInfo);
        UUID userId = userDTO.userId();

        userSocialTokenService.createUserSocialToken(userId);
        userApiKeyService.createUserApiKey(userId);

        return userDTO;
    }

}
