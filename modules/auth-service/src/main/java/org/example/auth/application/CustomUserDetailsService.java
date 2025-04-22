package org.example.auth.application;

import java.util.UUID;

import org.example.auth.application.dto.PrincipalDetails;
import org.example.user.application.dto.UserDto;
import org.example.user.application.port.UserReader;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserReader userReader;

    @Override
    public PrincipalDetails loadUserByUsername(String username) {
        UUID userId = UUID.fromString(username);
        UserDto userDto = userReader.loadUser(userId);


        return new PrincipalDetails(userDto, null, null);
    }
}
