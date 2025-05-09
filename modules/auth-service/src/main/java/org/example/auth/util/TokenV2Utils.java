package org.example.auth.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenV2Utils {

    private final SecretKeySpec secretKey;
    private final long expiration;

    public TokenV2Utils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
        this.expiration = expiration;
    }

    public String generateSessionToken(UserDetails userDetails) throws Exception {
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .claim("authorities", authorities)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expiration))
                .build();

        EncryptedJWT encryptedJWT = new EncryptedJWT(
                new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A192GCM)
                        .contentType("JWT")
                        .build(),
                claimsSet
        );

        DirectEncrypter encrypter = new DirectEncrypter(secretKey);
        encryptedJWT.encrypt(encrypter);

        return encryptedJWT.serialize(); // Compact JWE string
    }

    public UserDetails validateAndGetUserDetails(String token) throws Exception {
        EncryptedJWT encryptedJWT = EncryptedJWT.parse(token);

        DirectDecrypter decrypter = new DirectDecrypter(secretKey);
        encryptedJWT.decrypt(decrypter);

        JWTClaimsSet claims = encryptedJWT.getJWTClaimsSet();

        String username = claims.getSubject();

        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.getClaim("authorities");

        List<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(username, "", grantedAuthorities);
    }

    public long getTokenExpiration() {
        return expiration;
    }
}
