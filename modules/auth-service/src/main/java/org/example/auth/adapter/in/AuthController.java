package org.example.auth.adapter.in;

import java.util.UUID;

import org.example.auth.adapter.in.response.UserInfoResponse;
import org.example.auth.application.ApiKeyService;
import org.example.user.application.dto.UserDto;
import org.example.user.application.port.UserReader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증/인가")
public class AuthController {

    private final UserReader userOAuthReader;
    private final ApiKeyService apiKeyService;

    @GetMapping("/me")
    @Operation(summary = "유저 정보 조회", description = "유저를 식별할 수 있는 최소 정보 제공")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        UserDto userDto = userOAuthReader.loadUser(userId);
        UserInfoResponse response = new UserInfoResponse(userDto.userId(), userDto.name(), userDto.role());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api-key")
    @Operation(summary = "API 인증 키 발급", description = "외부에서 API 사용을 위한 유저 API 인증 키 발급")
    public ResponseEntity<String> issueApiKey(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        String apiKey = apiKeyService.issueApiKey(userId);
        return ResponseEntity.ok(apiKey);
    }

}
