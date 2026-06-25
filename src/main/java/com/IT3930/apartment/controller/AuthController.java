package com.IT3930.apartment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(
            jakarta.servlet.http.HttpServletRequest request,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            Object exception = request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            if (exception != null && exception.getClass().getSimpleName().equals("DisabledException")) {
                model.addAttribute("error", "Your account is not active. Please verify your email or wait for an admin to activate it.");
            } else {
                model.addAttribute("error", "Invalid email or password.");
            }
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    @Autowired
    private com.IT3930.apartment.service.AccountService accountService;

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, Model model) {
        boolean verified = accountService.verifyAccount(token);
        if (verified) {
            model.addAttribute("message", "Your account has been successfully verified! You can now log in.");
        } else {
            model.addAttribute("error", "Invalid or expired verification link.");
        }
        return "login";
    }
}
