package org.example.auth.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    public static class OAuth2 {

        private String redirectUri;

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }

        // Fluent-style method (optional)
        public OAuth2 redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }
    }

}
