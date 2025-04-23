package org.example.auth.application;

import java.util.Optional;

import org.example.auth.util.CookieUtils;
import org.example.auth.util.JwtUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenRefreshService {

    private final JwtUtils jwtUtil;
    private final CookieUtils cookieUtiles;
    private final CustomUserDetailsService customUserDetailsService;

    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
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

        return accessToken;
    }

}
