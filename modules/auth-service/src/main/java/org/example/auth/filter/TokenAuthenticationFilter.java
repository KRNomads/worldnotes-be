package org.example.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.auth.util.TokenV2Utils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenV2Utils tokenV2Utils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 쿠키에서 token_v2 추출
        String token = extractTokenFromCookie(request, "token_v2");

        if (token != null) {
            try {
                // 2. 복호화 및 검증
                UserDetails userDetails = tokenV2Utils.validateAndGetUserDetails(token);

                // 3. 인증 객체 생성 및 주입
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // 예외 발생 시 로그만 출력하고 통과 (401은 나중에 핸들러가 처리)
                log.warn("Invalid token_v2: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
