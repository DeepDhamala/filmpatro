package com.deepdhamala.filmpatro.email;


import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCodeEntity;
import com.deepdhamala.filmpatro.email.message.EmailMessage;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JavaMailEmailService implements EmailService{

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailMessage<?> emailMessage) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getHtmlContent(), true);

            javaMailSender.send(mimeMessage);
        } catch (EmailDeliveryException e) {
            throw new EmailDeliveryException("Failed to send password reset email: " + e.getMessage());
        } catch (Exception e){
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage(), e);
        }
    }


    @Deprecated
    public void sendPasswordResetEmailHtml(String to, String resetLink) {
        try {
            MimeMailMessage message = new MimeMailMessage(javaMailSender.createMimeMessage());
            message.setTo(to);
            message.setSubject("Password Reset Request");
            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<h2 style='color: #2e6c80;'>Password Reset Request</h2>" +
                    "<p>Hi there,</p>" +
                    "<p>We received a request to reset your password. Click the button below to reset it:</p>" +
                    "<a href='" + resetLink + "' " +
                    "style='display: inline-block; padding: 10px 20px; font-size: 16px; " +
                    "color: white; background-color: #007BFF; text-decoration: none; border-radius: 5px;'>Reset Password</a>" +
                    "<p>If you didn't request this, just ignore this email.</p>" +
                    "<p>Thanks,<br/>Film Patro Team</p>" +
                    "</body>" +
                    "</html>";
            message.setText(htmlContent);
            javaMailSender.send(message.getMimeMessage());
        } catch (EmailDeliveryException e) {
            throw new EmailDeliveryException("Failed to send password reset email: " + e.getMessage());
        } catch (Exception e){
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage(), e);
        }
    }

    @Deprecated
    public void sendEmailVerificationEmailHtml(String to, EmailVerificationCodeEntity emailVerificationCode) {
        try {
            String verificationLink = "http://localhost:8080/api/v2/auth/user/registration/verify-email?verificationCode=" + emailVerificationCode.getEmailVerificationToken();
            MimeMailMessage message = new MimeMailMessage(javaMailSender.createMimeMessage());
            message.setTo(to);
            message.setSubject("Verify Your Email - Film Patro");

            String htmlContent = """
                    <html>
                    <body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>
                        <div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>
                            <h2 style='color: #2e6c80;'>Welcome to Film Patro!</h2>
                            <p>Hello,</p>
                            <p>Thank you for registering. Please verify your email by clicking the button below:</p>
                            <p style='text-align: center; margin: 30px 0;'>
                                <a href='%s'
                                   style='display: inline-block; padding: 12px 24px; font-size: 16px; 
                                          color: white; background-color: #28a745; text-decoration: none; 
                                          border-radius: 5px;'>Verify Email</a>
                            </p>
                            <p>If you didn’t create an account, no further action is required.</p>
                            <p>Thanks,<br/>The Film Patro Team</p>
                            <hr style='margin: 30px 0;'>
                            <p style='font-size: 12px; color: #999;'>This link will expire in 30 minutes.</p>
                        </div>
                    </body>
                    </html>
                    """.formatted(verificationLink);

            message.getMimeMessage().setContent(htmlContent, "text/html; charset=utf-8");
            javaMailSender.send(message.getMimeMessage());

        } catch (Exception e) {
            throw new EmailDeliveryException("Failed to send email verification email: " + e.getMessage());
        }
    }

}
