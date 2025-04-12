package org.example.auth.handler;

import java.io.IOException;

import org.example.auth.application.dto.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final HttpSession httpSession;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if (authentication.getPrincipal() instanceof PrincipalDetails principalDetails) {
            // 세션에 사용자 정보 저장
            httpSession.setAttribute("user", principalDetails.getName());

            // 정상 로그인 시 홈 또는 지정된 URL로 리디렉트
            response.sendRedirect("/success");
        } else {
            throw new IllegalArgumentException("Authentication is not PrincipalDetails");
        }
    }
}
