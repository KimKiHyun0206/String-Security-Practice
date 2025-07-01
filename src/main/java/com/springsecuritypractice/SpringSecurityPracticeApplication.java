package com.springsecuritypractice;

import com.springsecuritypractice.config.filter.JwtCustomFilterAndVoterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@Import(SessionSecurityConfig.class)
//@Import(JwtSecurityConfig.class)
@Import(JwtCustomFilterAndVoterConfig.class)
public class SpringSecurityPracticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityPracticeApplication.class, args);
    }
}