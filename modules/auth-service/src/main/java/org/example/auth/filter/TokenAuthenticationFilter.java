package org.example.auth.filter;

import java.io.IOException;
import java.util.Arrays;

import org.example.auth.util.TokenV2Utils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenV2Utils tokenV2Utils;
    private static final String COOKIE_NAME = "token_v2";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 이미 인증된 사용자인지 확인
        if (isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. 쿠키에서 token_v2 추출
        String token = extractTokenFromCookie(request, COOKIE_NAME);

        if (token != null) {
            try {
                // 2. 토큰 검증 및 인증 처리
                authenticateWithToken(token);
            } catch (Exception e) {
                // 예외 발생 시 로그만 출력하고 통과
                log.warn("Token 인증 실패: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 토큰을 검증하고 인증 객체를 생성하여 SecurityContext에 설정
     */
    private void authenticateWithToken(String token) throws Exception {
        // 토큰 복호화 및 검증
        UserDetails userDetails = tokenV2Utils.validateAndGetUserDetails(token);

        // 인증 객체 생성 및 주입
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                )
        );

        log.debug("Token 인증 성공: {}", userDetails.getUsername());
    }

    /**
     * 현재 사용자가 이미 인증되었는지 확인
     */
    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    /**
     * 쿠키에서 토큰 값 추출
     */
    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
