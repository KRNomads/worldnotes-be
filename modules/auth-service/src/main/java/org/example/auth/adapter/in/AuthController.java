package org.example.auth.adapter.in;

import java.util.UUID;

import org.example.auth.adapter.in.response.AuthenticationResponse;
import org.example.auth.application.ApiKeyService;
import org.example.auth.application.TokenRefreshService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증/인가")
public class AuthController {

    private final ApiKeyService apiKeyService;
    private final TokenRefreshService tokenRefreshService;

    // 로그아웃
    @PostMapping("/refresh")
    @Operation(summary = "액세스 토큰 리프레시", description = "액세스 토큰 재발급")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authResponse = tokenRefreshService.refreshToken(request, response);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/api-key")
    @Operation(summary = "API 인증 키 발급", description = "외부에서 API 사용을 위한 유저 API 인증 키 발급")
    public ResponseEntity<String> issueApiKey(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        String apiKey = apiKeyService.issueApiKey(userId);
        return ResponseEntity.ok(apiKey);
    }

}
