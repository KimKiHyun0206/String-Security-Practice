package com.springsecuritypractice.config.filter;

import com.springsecuritypractice.filter.AdminFilter;
import com.springsecuritypractice.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class JwtCustomFilterConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public OncePerRequestFilter myCustomFilter() {
        return new AdminFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
                        // 인가 관련 별도 설정 없음
                        .anyRequest().authenticated()
                )
                // JWT 필터 등록
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 커스텀 필터 등록 (AdminFilter)
                .addFilterBefore(myCustomFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}