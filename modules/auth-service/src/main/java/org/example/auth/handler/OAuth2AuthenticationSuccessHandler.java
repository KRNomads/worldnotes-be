package org.example.auth.handler;

import java.io.IOException;
import java.util.Collection;

import org.example.auth.application.dto.PrincipalDetails;
import org.example.auth.util.AppProperties;
import org.example.auth.util.CookieUtils;
import org.example.auth.util.TokenV2Utils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenV2Utils tokenV2Utils;
    private final CookieUtils cookieUtils;
    private final AppProperties appProperties;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("응답이 이미 전송되었습니다. " + targetUrl + "로 리다이렉트 할 수 없습니다");
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        String redirectUri = (String) appProperties.getOauth2().getRedirectUri();

        if (redirectUri == null) {
            log.error("Invalid redirect URI. Falling back to default URL.");
            redirectUri = getDefaultTargetUrl();
        }

        // 사용자 정보 추출 및 token_v2 생성
        PrincipalDetails userPrincipal = (PrincipalDetails) authentication.getPrincipal();
        String tokenV2 = tokenV2Utils.generateSessionToken(userPrincipal);

        // token_v2 쿠키에 저장
        cookieUtils.addCookie(response, "token_v2", tokenV2, (int) (tokenV2Utils.getTokenExpiration() / 1000));

        Collection<String> setCookieHeaders = response.getHeaders("Set-Cookie");
        setCookieHeaders.forEach(cookieHeader
                -> log.info("Set-Cookie 헤더: {}", cookieHeader)
        );

        return UriComponentsBuilder.fromUriString(redirectUri)
                .build().toUriString();
    }
}
