package com.springsecuritypractice.student.service;

import com.springsecuritypractice.aop.LogReturn;
import com.springsecuritypractice.auth.domain.Authority;
import com.springsecuritypractice.student.domain.Student;
import com.springsecuritypractice.student.dto.request.StudentCreateRequest;
import com.springsecuritypractice.student.dto.request.StudentUpdateRequest;
import com.springsecuritypractice.student.dto.response.StudentResponse;
import com.springsecuritypractice.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @LogReturn
    @Transactional
    public StudentResponse create(StudentCreateRequest request) {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.getUserRole());

        Student entity = Student.toEntity(request, authorities);
        studentRepository.save(entity);
        return entity.toResponse();
    }

    @LogReturn
    @Transactional(readOnly = true)
    public StudentResponse read(String loginId) {
        return studentRepository
                .findOneByLoginId(loginId)
                .get()
                .toResponse();
    }

    @LogReturn
    @Transactional
    public StudentResponse update(String loginId, StudentUpdateRequest request) {
        Student student = studentRepository.findOneByLoginId(loginId).get();
        student.update(request);
        return student.toResponse();
    }

    @Transactional
    public void delete(String loginId) {
        if (studentRepository.existsByLoginId(loginId)) {
            studentRepository.deleteByLoginId(loginId);
        }
    }
}