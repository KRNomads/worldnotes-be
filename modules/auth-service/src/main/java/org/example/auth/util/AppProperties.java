package org.example.auth.util;

import java.util.ArrayList;
import java.util.List;

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

        private List<String> authorizedRedirectUris = new ArrayList<>();

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

}
