package org.example.auth.handler;

import java.io.IOException;

import org.example.auth.application.dto.PrincipalDetails;
import org.example.auth.util.AppProperties;
import org.example.auth.util.CookieUtils;
import org.example.auth.util.JwtUtils;
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

    private final JwtUtils jwtUtil;
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

        // redirectUri 유효성 검증
        if (redirectUri == null) {
            log.error("Unauthorized or invalid Redirect URI: '{}'. Falling back to default target URL.", redirectUri);
            redirectUri = getDefaultTargetUrl(); // 안전하게 기본 URL 사용
        }

        // 토큰 생성
        PrincipalDetails userPrincipal = (PrincipalDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateToken(userPrincipal);
        String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);

        // 리프레시 토큰을 HttpOnly 쿠키에 저장
        cookieUtils.addCookie(response, "refresh_token", refreshToken, (int) (jwtUtil.getRefreshExpiration() / 1000));

        // 액세스 토큰을 URL 파라미터로 전달
        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", accessToken)
                .build().toUriString();
    }

}
