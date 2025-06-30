package com.springsecuritypractice.UIController;

import com.springsecuritypractice.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UIController {

    private final StudentService studentService;

    @RequestMapping
    public String home() {
        return "home";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/name")
    public String name(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        model.addAttribute("name", userDetails.getUsername());
        return "name";
    }
}
