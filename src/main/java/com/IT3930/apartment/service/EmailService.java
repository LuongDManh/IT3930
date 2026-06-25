package com.IT3930.apartment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public void sendVerificationEmail(String email, String token) {
        String verificationUrl = baseUrl + "/verify?token=" + token;
        
        try {
            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Verify your account");
            helper.setText("Please click the link to verify your account: <a href='" + verificationUrl + "'>" + verificationUrl + "</a>", true);
            
            mailSender.send(message);
            logger.info("Verification email successfully sent to: " + email);
        } catch (Exception e) {
            logger.error("Failed to send email to " + email, e);
            // Optionally, still print it to console so you can test if SMTP is invalid
            logger.info("Verification Link (fallback): " + verificationUrl);
        }
    }
}
