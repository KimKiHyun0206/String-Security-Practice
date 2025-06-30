package com.springsecuritypractice.config;

import com.springsecuritypractice.jwt.JwtFilter;
import com.springsecuritypractice.jwt.JwtTokenProvider;
import com.springsecuritypractice.login.controller.JwtLoginController;
import com.springsecuritypractice.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequiredArgsConstructor
public class JwtSecurityConfig {

    private final LoginService loginService;

    @Value("${jwt.secret}")
    String secret;

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtTokenProvider());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secret);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(loginService)
                .passwordEncoder(new BCryptPasswordEncoder())
                .and()
                .build();
    }

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
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}