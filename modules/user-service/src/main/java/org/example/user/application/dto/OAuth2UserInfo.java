package org.example.user.application.dto;

import java.util.Map;

import org.example.common.enums.SocialProvider;
import org.example.user.domain.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2UserInfo {

    private SocialProvider provider;
    private String providerId;
    private String name;
    private String email;
    private String profileImg;

    public static OAuth2UserInfo of(SocialProvider socialProvider, Map<String, Object> attributes) {
        return switch (socialProvider) {
            case GOOGLE ->
                ofGoogle(socialProvider, attributes);
            case KAKAO ->
                ofKakao(socialProvider, attributes);
            default ->
                throw new IllegalArgumentException("Unsupported Social Provider: " + socialProvider);
        };
    }

    private static OAuth2UserInfo ofGoogle(SocialProvider socialProvider, Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider(socialProvider)
                .providerId(attributes.get("sub").toString())
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImg((String) attributes.get("picture"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(SocialProvider socialProvider, Map<String, Object> attributes) {
        // 예시로 하드코딩된 부분, 실제로는 attributes 파싱 필요
        // Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        // Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .provider(socialProvider)
                .providerId(attributes.get("id").toString())
                // .name((String) profile.get("nickname"))
                // .email((String) account.get("email"))
                .name("test")
                .email("test@example.com")
                .build();
    }

    public User toEntity() {
        return User.create(name, email, provider, providerId);
    }
}
