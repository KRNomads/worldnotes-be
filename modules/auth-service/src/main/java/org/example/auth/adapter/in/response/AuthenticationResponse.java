package org.example.auth.adapter.in.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String accessToken
        ) {

}
