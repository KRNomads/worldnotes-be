package org.example.auth.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import org.example.auth.application.ApiKeyService;
import org.example.auth.domain.exception.ApiKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;
    private static final String HEADER_NAME = "X-API-KEY";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 이미 인증된 사용자인지 확인
        if (isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. 헤더에서 API 키 추출
        String apiKey = extractApiKeyFromHeader(request);

        if (apiKey != null) {
            try {
                // 2. API 키 검증 및 인증 처리
                authenticateWithApiKey(apiKey);
            } catch (ApiKeyException ex) {
                // 로그만 남기고 다음 필터로 진행
                log.warn("API Key 인증 실패: {}", ex.getMessage());
            } catch (Exception ex) {
                // 기타 예외도 로그만 남기고 진행
                log.error("API Key 인증 중 오류 발생", ex);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * API 키를 검증하고 인증 객체를 생성하여 SecurityContext에 설정
     */
    private void authenticateWithApiKey(String apiKey) {
        // API 키 검증
        UUID userId = apiKeyService.validateApiKey(apiKey);

        // 사용자 객체 생성
        User principal = new User(
                userId.toString(),
                "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // 인증 객체 생성 및 주입
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                )
        );

        log.debug("API Key 인증 성공: {}", userId);
    }

    /**
     * 현재 사용자가 이미 인증되었는지 확인
     */
    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    /**
     * 요청 헤더에서 API 키 추출
     */
    private String extractApiKeyFromHeader(HttpServletRequest request) {
        return request.getHeader(HEADER_NAME);
    }
}
