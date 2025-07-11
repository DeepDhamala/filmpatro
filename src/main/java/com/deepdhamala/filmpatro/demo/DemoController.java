package com.deepdhamala.filmpatro.demo;

import com.deepdhamala.filmpatro.email.JavaMailEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class DemoController {
    private final JavaMailEmailService emailService;

    @RequestMapping("/hello")
    public String securedDemo(@AuthenticationPrincipal UserDetails userDetails) {
        return "Hello, " + (userDetails != null ? userDetails.getUsername() : "Guest") +
                "! You are authenticated and can access secured endpoints.";
    }

}
