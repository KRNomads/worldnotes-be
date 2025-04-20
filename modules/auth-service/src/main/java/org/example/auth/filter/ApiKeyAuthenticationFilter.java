package org.example.auth.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import org.example.auth.application.ApiKeyService;
import org.example.auth.domain.exception.ApiKeyException;
import org.example.common.exception.ErrorCode;
import org.example.common.exception.ErrorResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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

    private final ObjectMapper objectMapper;

    private static final String HEADER_NAME = "X-API-KEY";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader(HEADER_NAME);

        if (apiKey == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UUID userId = apiKeyService.validateApiKey(apiKey);
            setAuthentication(userId);
        } catch (ApiKeyException ex) {
            log.error("API Key 인증 실패", ex);

            ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode())
                    .withCustomMessage(ex.getMessage());

            writeErrorResponse(response, errorResponse);
            return;
        } catch (Exception ex) {
            log.error("필터 내부 예기치 않은 에러 발생", ex);

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
            writeErrorResponse(response, errorResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }

    // 인증객체 설정
    private void setAuthentication(UUID userId) {
        Authentication authentication = getAuthenticationFromUserId(userId);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // userId로 인증 객체 생성
    private Authentication getAuthenticationFromUserId(UUID userId) {

        // 고정된 ROLE_USER 권한 부여
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        // userId를 username으로 설정 (필요에 따라 다르게 설정 가능)
        User principal = new User(userId.toString(), "", Collections.singletonList(authority));

        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    // 에러 응답 객체 전송
    private void writeErrorResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(errorResponse.getStatusCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
