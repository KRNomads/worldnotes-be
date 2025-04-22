package org.example.auth.handler;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.example.auth.application.dto.PrincipalDetails;
import org.example.auth.util.AppProperties;
import org.example.auth.util.CookieUtils;
import org.example.auth.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
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
            logger.debug("응답이 이미 전송되었습니다. " + targetUrl + "로 리다이렉트 할 수 없습니다");
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        Optional<String> redirectUri = cookieUtils.getCookie(request, "redirect_uri")
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("승인되지 않은 리다이렉트 URI가 있어 인증을 진행할 수 없습니다");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        // 토큰 생성
        PrincipalDetails userPrincipal = (PrincipalDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateToken(userPrincipal);
        String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);

        // 리프레시 토큰을 HttpOnly 쿠키에 저장
        cookieUtils.addCookie(response, "refresh_token", refreshToken, (int) (jwtUtil.getRefreshExpiration() / 1000));

        // 액세스 토큰을 URL 파라미터로 전달
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .build().toUriString();
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .map(URI::create)
                .anyMatch(authorizedUri
                        -> authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort()
                );
    }
}
