package org.example.common.enums;

public enum SocialProvider {

    KAKAO("kakao", false),
    GOOGLE("google", true);

    private final String providerName;
    private final Boolean isSaveRefreshToken;

    SocialProvider(String providerName, Boolean isSaveRefreshToken) {
        this.providerName = providerName;
        this.isSaveRefreshToken = isSaveRefreshToken;
    }

    // 코드값으로 SocialProvider 찾기
    public static SocialProvider fromCode(String providerName) {
        for (SocialProvider provider : values()) {
            if (provider.providerName.equals(providerName)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Invalid code for SocialProvider: " + providerName);
    }

}
