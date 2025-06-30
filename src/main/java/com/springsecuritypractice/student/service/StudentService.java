package com.springsecuritypractice.student.service;

import com.springsecuritypractice.auth.domain.Authority;
import com.springsecuritypractice.student.domain.Student;
import com.springsecuritypractice.student.dto.request.StudentCreateRequest;
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

    @Transactional
    public StudentResponse create(StudentCreateRequest request){
        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.getUserRole());

        Student entity = Student.toEntity(request, authorities);
        studentRepository.save(entity);
        StudentResponse response = entity.toResponse();
        log.info(response.toString());
        return response;
    }
}