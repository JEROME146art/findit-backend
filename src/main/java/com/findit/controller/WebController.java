package com.findit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serves HTML pages (Thymeleaf templates).
 * Uses @Controller (NOT @RestController) — returns view names, not JSON.
 */
@Controller
public class WebController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/report")
    public String report() {
        return "report";
    }

    @GetMapping("/matches")
    public String matches() {
        return "matches";
    }

    @GetMapping("/browse")
    public String browse() {
        return "browse";
    }

    @GetMapping({"/voting", "/univote"})
    public String voting() {
        return "voting";
    }
}