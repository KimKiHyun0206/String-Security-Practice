package com.springsecuritypractice.login.service;

import com.springsecuritypractice.student.domain.Student;
import com.springsecuritypractice.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
   private final StudentRepository userRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String loginId) {
      return userRepository.findOneWithAuthoritiesByLoginId(loginId)
         .map(user -> createUser(loginId, user))
         .orElseThrow(() -> new UsernameNotFoundException(loginId + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createUser(String username, Student user) {

      List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toList());

      return new org.springframework.security.core.userdetails.User(
              user.getLoginId(),
              user.getPassword(),
              grantedAuthorities
      );
   }
}