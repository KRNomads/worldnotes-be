package org.example.user.application;

import java.util.UUID;

import org.example.common.enums.SocialProvider;
import org.example.common.exception.ErrorCode;
import org.example.user.adapter.out.UserRepository;
import org.example.user.application.dto.OAuth2UserInfo;
import org.example.user.application.dto.UserDto;
import org.example.user.domain.entity.User;
import org.example.user.domain.exception.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ==== 조회 ====
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID userId) {
        return UserDto.from(getUserOrThrow(userId));
    }

    @Transactional(readOnly = true)
    public UserDto getUserByProvider(SocialProvider provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId)
                .map(UserDto::from)
                .orElse(null);
    }

    // ==== 생성 ====
    @Transactional
    public UserDto createUser(OAuth2UserInfo oAuth2UserInfo) {
        User user = oAuth2UserInfo.toEntity();
        userRepository.save(user);
        return UserDto.from(user);
    }

    // ==== 수정 ====
    @Transactional
    public UserDto updateUserNameAndEmail(UUID userId, String name, String email) {
        User user = getUserOrThrow(userId);
        user.updateNameAndEmail(name, email);
        return UserDto.from(user);
    }

    @Transactional
    public UserDto updateUserProfileImg(UUID userId, String profileImg) {
        User user = getUserOrThrow(userId);
        user.updateProfileImg(profileImg);
        return UserDto.from(user);
    }

    // ==== 삭제 ====
    @Transactional
    public void deleteUser(UUID userId) {
        User user = getUserOrThrow(userId);
        user.deleteUser();
    }

    @Transactional
    public void hardDeleteUser(UUID userId) {
        User user = getUserOrThrow(userId);
        user.hardDeleteUser();
    }

    // ==== 복구 ====
    @Transactional
    public UserDto recoveryUser(UUID userId) {
        User user = getUserOrThrow(userId);
        user.recoveryUser();
        return UserDto.from(user);
    }

    // ==== 공통 유틸 ====
    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, userId));
    }

}
