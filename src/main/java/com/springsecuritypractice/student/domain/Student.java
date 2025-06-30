package com.springsecuritypractice.student.domain;

import com.springsecuritypractice.auth.domain.Authority;
import com.springsecuritypractice.student.dto.request.StudentCreateRequest;
import com.springsecuritypractice.student.dto.response.StudentResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;
    private String loginId;
    private String password;
    private String name;

    @ManyToMany
    @JoinTable(
            name = "student_authority",
            joinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;


    @Builder(access = AccessLevel.PRIVATE)
    private Student(String loginId, String password, String name, Set<Authority> authorities) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.authorities = authorities;
    }

    public static Student toEntity(StudentCreateRequest request, Set<Authority> authorities) {
        return Student.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .name(request.getName())
                .authorities(authorities)
                .build();
    }

    public StudentResponse toResponse() {
        return StudentResponse.builder()
                .id(this.id)
                .loginId(this.loginId)
                .password(this.password)
                .name(this.name)
                .authorities(this.authorities)
                .build();
    }
}