package com.springsecuritypractice;

import com.springsecuritypractice.config.filter.JwtCustomFilterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling   // 스케쥴링 기능 활성화
@SpringBootApplication
//@Import(SessionSecurityConfig.class)
//@Import(JwtSecurityConfig.class)
@Import(JwtCustomFilterConfig.class)
public class SpringSecurityPracticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityPracticeApplication.class, args);
    }
}