package com.example.demo.service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Send HTML Email
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        try {
            // Create a new MimeMessage
            var message = mailSender.createMimeMessage();
            
            // Use MimeMessageHelper to build the message with HTML content
            var helper = new MimeMessageHelper(message, true); // 'true' indicates multipart (for HTML content)
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // 'true' means HTML content
            helper.setFrom("hotel-booking-system@example.com"); // Replace with your sender email

            // Send the email
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
