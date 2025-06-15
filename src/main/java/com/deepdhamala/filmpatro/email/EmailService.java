package com.deepdhamala.filmpatro.email;

import com.deepdhamala.filmpatro.auth.token.emailVerification.EmailVerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendOtpEmail(String to, String otp){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp + "\nThis code will expire in 5 minutes.");
            javaMailSender.send(message);
        }
        catch (Exception e){
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendEmailVerificationEmail(String email, EmailVerificationToken emailVerificationToken) {
        String link = "http://localhost:8080/api/v1/auth/user/verify-email?token=" + emailVerificationToken.getEmailVerificationToken();
        String subject = "Email Verification";
        String text = "Please click the following link to verify your email: " + link + "\nThis link will expire in 30 minutes.";
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email verification email: " + e.getMessage());
        }
    }
}
