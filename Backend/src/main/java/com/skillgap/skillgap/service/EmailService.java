package com.skillgap.skillgap.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String toEmail, String otp) {
        try {
            System.out.println("📩 Sending OTP to: " + toEmail);
            System.out.println("OTP: " + otp);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("SkillGap Email Verification OTP");
            message.setText("Your OTP for SkillGap account verification is: " + otp);

            mailSender.send(message);

            System.out.println("✅ Email sent successfully");

        } catch (Exception e) {
            System.out.println("❌ EMAIL FAILED");
            e.printStackTrace();
        }
    }
//    public void sendOtp(String toEmail, String otp) {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject("SkillGap Email Verification OTP");
//        message.setText("Your OTP for SkillGap account verification is: " + otp);
//
//        mailSender.send(message);
//    }
}