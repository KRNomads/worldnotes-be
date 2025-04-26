package org.example.auth.config;

import org.example.auth.application.CustomOAuth2UserService;
import org.example.auth.filter.ApiKeyAuthenticationFilter;
import org.example.auth.filter.TokenAuthenticationFilter;
import org.example.auth.handler.CustomAccessDeniedHandler;
import org.example.auth.handler.CustomAuthenticationEntryPoint;
import org.example.auth.handler.OAuth2AuthenticationSuccessHandler;
import org.example.auth.handler.OAuth2FailureHandler;
import org.example.auth.handler.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer; // Customizer 임포트 추가
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration; // CORS 관련 임포트 추가
import org.springframework.web.cors.CorsConfigurationSource; // CORS 관련 임포트 추가
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // CORS 관련 임포트 추가

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import java.util.List; // List 임포트 추가

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http, ClientRegistrationRepository repo) throws Exception {
        http
                // 기본 보안 설정
                .csrf(AbstractHttpConfigurer::disable) // 필요시 enable
                // --- CORS 설정 수정 ---
                // .cors(AbstractHttpConfigurer::disable) // 이 줄을 제거하거나 아래와 같이 변경
                .cors(Customizer.withDefaults())      // 정의된 CorsConfigurationSource Bean을 사용하도록 변경
                // ----------------------
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                       .addHeaderWriter(
                               new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                // 세션 방식
                .sessionManagement(session -> session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 권한 설정
                .authorizeHttpRequests(auth -> auth
                       .requestMatchers(
                               new AntPathRequestMatcher("/"),
                               new AntPathRequestMatcher("/swagger-ui/**"),
                               new AntPathRequestMatcher("/v3/api-docs/**"),
                               new AntPathRequestMatcher("/api/v1/auth/refresh"))
                       .permitAll()
                       .anyRequest().hasAnyRole("USER", "ADMIN"))
                // OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                       .userInfoEndpoint(info -> info.userService(oAuth2UserService))
                       .successHandler(oAuth2AuthenticationSuccessHandler)
                       .failureHandler(oAuth2FailureHandler))
                // 로그아웃 설정
                .logout(logout -> logout
                       .logoutUrl("/logout")
                       .logoutSuccessUrl("https://localhost:3000/") // 프론트엔드 주소로 변경 고려
                       .addLogoutHandler((request, response, authentication) -> {
                           HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
                           if (session != null) {
                              session.invalidate();
                           }
                       })
                       .logoutSuccessHandler((request, response, authentication)
                               -> response.sendRedirect("https://localhost:3000/")) // 프론트엔드 주소로 변경 고려
                       .deleteCookies("JSESSIONID", "refresh_token")) // 쿠키 이름 확인 필요
                // 필터 설정
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 인증 예외 핸들링
                .exceptionHandling(ex -> ex
                       .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                       .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    // --- CORS 설정 Bean 주석 해제 ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트엔드 주소
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // OPTIONS 포함 확인
        configuration.setAllowedHeaders(List.of("*")); // 필요시 특정 헤더만 허용하도록 수정
        configuration.setAllowCredentials(true); // Credentials 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용
        return source;
    }
    // ---------------------------------
}