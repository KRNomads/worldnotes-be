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

    // 유저 정보 로드 
    public UserDto loadUser(OAuth2UserInfo oAuth2UserInfo) {
        SocialProvider provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();
        String profileImg = oAuth2UserInfo.getProfileImg();

        // 유저가 존재할 경우 로드, 없으면 회원가입
        UserDto userDto = userService.getUserByProvider(provider, providerId);
        userDto = userDto != null ? userDto : registUser(oAuth2UserInfo);

        // 유저 이메일, 이름 검증 후 다를 시 업데이트
        if (!userDto.name().equals(name) || !userDto.email().equals(email)) {
            userDto = userService.updateUserNameAndEmail(userDto.userId(), name, email);
        }

        // 프로필 이미지가 다를 때 업데이트
        if (!equalsNullable(userDto.profileImg(), profileImg)) {
            userDto = userService.updateUserProfileImg(userDto.userId(), profileImg);
        }

        return userDto;
    }

    // 회원가입
    @Transactional
    private UserDto registUser(OAuth2UserInfo oAuth2UserInfo) {
        UserDto userDTO = userService.createUser(oAuth2UserInfo);
        UUID userId = userDTO.userId();

        userSocialTokenService.createUserSocialToken(userId);

        return userDTO;
    }

    private boolean equalsNullable(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        }
        return s1.equals(s2);
    }

    // 유저 업데이트 추가 할거?
    // private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    //     existingUser.setFirstName(oAuth2UserInfo.getName());
    //     existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
    //     return userRepository.save(existingUser);
    // }
}
