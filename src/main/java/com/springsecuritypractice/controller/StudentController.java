package com.springsecuritypractice.controller;

import com.springsecuritypractice.student.dto.request.StudentCreateRequest;
import com.springsecuritypractice.student.dto.request.StudentUpdateRequest;
import com.springsecuritypractice.student.dto.response.StudentResponse;
import com.springsecuritypractice.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


/**
 * Student를 조회하는 간단한 CRUD 컨트롤러
 *
 * @author duskafka
 * @see StudentService      서비스 계층 클래스
 * */
@Slf4j
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @Operation(summary = "student 생성")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentResponse> create(@RequestBody StudentCreateRequest request){
        return ResponseEntity.ok(studentService.create(request));
    }

    @Operation(summary = "student 수정")
    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudentResponse> update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody StudentUpdateRequest request){
        return ResponseEntity.ok(studentService.update(userDetails.getUsername(), request));
    }

    @Operation(summary = "student 조회")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudentResponse> get(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(studentService.read(userDetails.getUsername()));
    }

    @Operation(summary = "student 삭제")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudentResponse> delete(@AuthenticationPrincipal UserDetails userDetails) {
        studentService.delete(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}