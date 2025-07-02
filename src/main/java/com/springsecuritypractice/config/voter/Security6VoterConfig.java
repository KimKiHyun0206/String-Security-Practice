package com.springsecuritypractice.config.voter;

import com.springsecuritypractice.voter.security6.manager.CustomAuthorizationManager;
import com.springsecuritypractice.voter.security6.IpAddressVoter;
import com.springsecuritypractice.voter.security6.policy.AffirmativePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@EnableWebSecurity
public class Security6VoterConfig {

    @Bean
    public CustomAuthorizationManager customAuthorizationManager() {
        return new CustomAuthorizationManager(
                List.of(new IpAddressVoter()),
                new AffirmativePolicy()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            CustomAuthorizationManager customAuthorizationManager
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").access(customAuthorizationManager)
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}