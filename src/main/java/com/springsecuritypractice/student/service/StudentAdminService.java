package com.springsecuritypractice.student.service;

import com.springsecuritypractice.auth.domain.Authority;
import com.springsecuritypractice.student.domain.Student;
import com.springsecuritypractice.student.dto.request.StudentUpdateRequest;
import com.springsecuritypractice.student.dto.response.StudentResponse;
import com.springsecuritypractice.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentAdminService {
    private final StudentRepository studentRepository;

    @Transactional
    public void addAdminToUser(Long id){
        Student student = studentRepository.findById(id).get();
        student.getAuthorities().add(new Authority("ROLE_ADMIN"));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> readAll(){
        return studentRepository
                .findAll()
                .stream()
                .map(Student::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse readById(Long id){
        return studentRepository
                .findById(id)
                .get()
                .toResponse();
    }

    @Transactional
    public StudentResponse update(Long id, StudentUpdateRequest request){
        Student student = studentRepository.findById(id).get();
        student.update(request);
        return student.toResponse();
    }

    @Transactional
    public void delete(Long id){
        if(studentRepository.existsById(id)){
            studentRepository.deleteById(id);
        }
    }
}