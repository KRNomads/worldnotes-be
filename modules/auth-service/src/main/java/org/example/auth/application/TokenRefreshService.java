package org.example.auth.application;

import java.util.Optional;

import org.example.auth.adapter.in.response.AuthenticationResponse;
import org.example.auth.util.CookieUtils;
import org.example.auth.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenRefreshService {

    private final JwtUtil jwtUtil;
    private final CookieUtils cookieUtiles;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> refreshTokenCookie = cookieUtiles.getCookie(request, "refresh_token");

        if (refreshTokenCookie.isEmpty()) {
            throw new IllegalArgumentException("Refresh token is missing");
        }

        String refreshToken = refreshTokenCookie.get().getValue();

        String userId = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

        if (!jwtUtil.validateToken(refreshToken, userDetails)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String accessToken = jwtUtil.generateToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        cookieUtiles.addCookie(response, "refresh_token", newRefreshToken, (int) (jwtUtil.getRefreshExpiration() / 1000));

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
    }

}
