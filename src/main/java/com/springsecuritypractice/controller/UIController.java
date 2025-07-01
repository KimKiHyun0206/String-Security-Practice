package com.springsecuritypractice.controller;

import com.springsecuritypractice.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UIController {
    @RequestMapping
    public String home() {
        return "home";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/auths")
    public String auths(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("authorities", authorities);

        return "auth_list";
    }
}
