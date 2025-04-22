package org.example.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Authorization 헤더가 없거나 Bearer 토큰이 아닌 경우 다음 필터로 진행
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 프리픽스 이후의 JWT 토큰 추출
        jwt = authHeader.substring(7);
        
        try {
            // JWT에서 사용자 이메일 추출
            userEmail = jwtUtil.extractUsername(jwt);
            
            // SecurityContext에 인증 정보가 없고, 토큰에서 사용자 이메일을 추출했을 경우
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 데이터베이스에서 사용자 세부 정보 로드
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                // 토큰이 유효한지 검증
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    // 인증 객체 생성 및 SecurityContext에 설정
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            logger.error("인증 처리 중 오류 발생: {}", e.getMessage());
            // 예외 발생 시 인증 처리 생략하고 다음 필터 진행
        }
        
        filterChain.doFilter(request, response);
    }
}