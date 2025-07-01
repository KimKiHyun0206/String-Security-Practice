package com.springsecuritypractice.controller;

import com.springsecuritypractice.student.dto.request.StudentUpdateRequest;
import com.springsecuritypractice.student.service.StudentAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/students")
public class StudentAdminController {
    private final StudentAdminService studentAdminService;

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(studentAdminService.readById(id));
    }

    @GetMapping
    public ResponseEntity<?> readAll() {
        return ResponseEntity.ok(studentAdminService.readAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody StudentUpdateRequest request) {
        return ResponseEntity.ok(studentAdminService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        studentAdminService.delete(id);
        return ResponseEntity.noContent().build();
    }
}