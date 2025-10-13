package com.money.manager.money_manager_api.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(body, true); // 'true' indicates the body is HTML
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(fromEmail);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            // For now, we'll just throw a runtime exception if it fails
            throw new RuntimeException("Failed to send email", e);
        }
    }
}