package com.springsecuritypractice.csrf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsrfTokenJpaRepository extends JpaRepository<CsrfTokenEntity, String> {
}