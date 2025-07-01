package com.springsecuritypractice.config;

import com.springsecuritypractice.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 애플리케이션에서 사용할 AuthenticationManager, PasswordEncoder를 등록한다.
 * 이는 애플리케이션에서 세션 기반 인증이던 JWT 기반 인증이던 공통적으로 사용되기 때문에 따로 분리해두었다.
 *
 * @author duskafka
 * */
@Configuration
@RequiredArgsConstructor
public class DefaultConfig {
    private final LoginService loginService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(loginService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
