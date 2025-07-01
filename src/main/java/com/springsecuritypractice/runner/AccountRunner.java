package com.springsecuritypractice.runner;

import com.springsecuritypractice.student.dto.request.StudentCreateRequest;
import com.springsecuritypractice.student.dto.response.StudentResponse;
import com.springsecuritypractice.student.service.StudentAdminService;
import com.springsecuritypractice.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountRunner implements ApplicationRunner {
    private final StudentService studentService;
    private final StudentAdminService studentAdminService;

    @Override
    public void run(ApplicationArguments args) {
        StudentResponse adminResponse = studentService.create(new StudentCreateRequest("admin", "admin", "ADMIN 계정"));
        studentAdminService.addAdminToUser(adminResponse.getId());
        log.info("[AccountRunner] ADMIN 계정 생성 {}", adminResponse);

        StudentResponse studentResponse = studentService.create(new StudentCreateRequest("test", "test", "TEST 계정"));
        log.info("[AccountRunner] TEST 계정 생성 {}", studentResponse);
    }
}