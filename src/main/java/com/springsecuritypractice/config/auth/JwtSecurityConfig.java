package com.springsecuritypractice.config.auth;

import com.springsecuritypractice.jwt.JwtFilter;
import com.springsecuritypractice.jwt.JwtTokenProvider;
import com.springsecuritypractice.controller.JwtLoginController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * JWT 토큰을 사용하기 위한 SecurityConfig.
 *
 * @author duskafka
 * @see JwtFilter           요청 헤더에서 Authorization을 가져와서 앞의 Bearer을 제거하고 토큰으로 Authentication을 가져와 SecurityContextHolder에 Authentication을 저장함.
 * @see JwtTokenProvider    토큰 발급과 토큰에서 Authentication을 가져오는 로직이 있다.
 * @see JwtLoginController  로그인 API와 @AuthenticationPrincipal를 사용해서 UserDetails를 받아올 수 있는 API가 있다.
 */
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class JwtSecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/**",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/swagger-ui.html",
                                "/login",
                                "/login/**",
                                "/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}