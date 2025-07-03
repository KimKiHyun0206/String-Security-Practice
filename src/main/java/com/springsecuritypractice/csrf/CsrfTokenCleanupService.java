package com.springsecuritypractice.csrf;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CsrfTokenCleanupService {

    private final CsrfTokenJpaRepository repository;

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void cleanupExpiredTokens() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30); // 30분 전
        repository.deleteByCreatedAtBefore(threshold);
    }
}