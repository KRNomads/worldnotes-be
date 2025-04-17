package org.example.auth.adapter.in;

import java.util.UUID;

import org.example.auth.application.ApiKeyIssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증/인가")
public class AuthController {

    private ApiKeyIssueService apiKeyIssueService;

    @PostMapping("/api-key")
    @Operation(summary = "API 인증 키 발급", description = "외부에서 API 사용을 위한 유저 API 인증 키 발급")
    public ResponseEntity<String> issueApiKey(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        String apiKey = apiKeyIssueService.issueApiKey(userId);
        return ResponseEntity.ok(apiKey);
    }

}
