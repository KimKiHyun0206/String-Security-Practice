package com.springsecuritypractice.controller;

import com.springsecuritypractice.jwt.JwtTokenProvider;
import com.springsecuritypractice.login.dto.LoginRequest;
import com.springsecuritypractice.login.dto.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
public class JwtLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword())
        );

        String token = jwtTokenProvider.createToken(authentication);
        response.setHeader("Authorization", "Bearer " + token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/result")
    public ResponseEntity<?> jwtResult(@AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails.toString());
        return ResponseEntity.ok(userDetails);
    }
}