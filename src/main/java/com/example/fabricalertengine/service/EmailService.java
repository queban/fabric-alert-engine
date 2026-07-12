package com.example.fabricalertengine.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    // Constructor
    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendAlert(String to, String fabricName){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Fabric price alert");
        message.setText(fabricName + " has hit your target price!");
        mailSender.send(message);
    }
}
