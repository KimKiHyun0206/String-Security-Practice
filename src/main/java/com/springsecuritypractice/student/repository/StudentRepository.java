package com.springsecuritypractice.student.repository;

import com.springsecuritypractice.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findOneWithAuthoritiesByLoginId(String loginId);
    Optional<Student> findOneByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    void deleteByLoginId(String loginId);
}