package com.springsecuritypractice.csrf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CsrfTokenJpaRepository extends JpaRepository<CsrfTokenEntity, String> {
    void deleteByCreatedAtBefore(LocalDateTime threshold);
}