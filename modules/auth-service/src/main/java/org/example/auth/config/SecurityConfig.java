package org.example.auth.config;

import org.example.auth.application.CustomOAuth2UserService;
import org.example.auth.filter.ApiKeyAuthenticationFilter;
import org.example.auth.filter.JwtAuthenticationFilter;
import org.example.auth.handler.CustomAccessDeniedHandler;
import org.example.auth.handler.CustomAuthenticationEntryPoint;
import org.example.auth.handler.OAuth2AuthenticationSuccessHandler;
import org.example.auth.handler.OAuth2FailureHandler;
import org.example.auth.handler.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                // 기본 보안 설정
                .csrf(AbstractHttpConfigurer::disable) // 필요시 enable
                .cors(AbstractHttpConfigurer::disable) // 프론트 연동 시 설정
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
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
                .logoutSuccessUrl("http://localhost:3000/")
                .addLogoutHandler((request, response, authentication) -> { // 로그아웃 핸들러 추가 (세션 무효화 처리)
                    HttpSession session = request.getSession();
                    session.invalidate();
                })
                .logoutSuccessHandler((request, response, authentication) // 로그아웃 성공 핸들러 추가 (리다이렉션 처리)
                        -> response.sendRedirect("/index"))
                .deleteCookies("JSESSIONID", "refresh_token"))
                // 필터 설정
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 인증 예외 핸들링
                .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 실패
                .accessDeniedHandler(new CustomAccessDeniedHandler())); // 인가 실패

        return http.build();
    }

    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트엔드 주소
    //     configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    //     configuration.setAllowedHeaders(List.of("*"));
    //     configuration.setAllowCredentials(true);
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }
}
