package com.artista.main.domain.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Value("${spring.profile}")
    private String profiles;

    @GetMapping("/port-profile")
    public String profile() {
        return profiles;
    }

    @GetMapping("/health")
    public String healtCheck() {
        return profiles;
    }

    @PostMapping("/post/health")
    public String postHealtCheck() {
        return "OK!";
    }
}