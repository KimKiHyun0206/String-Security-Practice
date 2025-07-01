package com.springsecuritypractice.config;

import com.springsecuritypractice.controller.UIController;
import com.springsecuritypractice.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


/**
 * 세션 기반 로그인을 구현한 SecurityConfig.
 * 아래에서 설정한 로그인 URL로 POST 요청이 오면 UsernamePasswordAuthenticationFiler가 요청을 가로채서 로그인을 수행한다.
 *
 * @author duskafka
 * @see UIController    로그인 페이지 등을 보여줄 Controller
 * @see UsernamePasswordAuthenticationFilter 요청을 가로챌 필터
 * */
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SessionSecurityConfig {
    private final CorsFilter corsFilter;
    private final LoginService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()).and().build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 개발용이며 실제 서비스 시에는 설정이 필요하다.
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(
                                "/api/**",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/swagger-ui.html",
                                "/login",
                                "/login/**",
                                "/**"
                        ).permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login") // 커스텀 로그인 페이지 경로
                        .defaultSuccessUrl("/auths", true)
                        .usernameParameter("username")  //원한다면 loginId 등으로 변경 가능
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login")        // 세션이 만료되었을 시 이동할 URL
                        .sessionFixation().migrateSession() // 로그인 시점에 세션 ID를 새로 발급해서 세션 탈취를 방지
                        .maximumSessions(1)                 // 한 계정으로 하나의 세션만 유지 (동시 로그인 제한)
                        .maxSessionsPreventsLogin(false)    // 기존 세션 만료 후 로그인 허용
                )
                // enable h2-console
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
        return http.build();
    }
}