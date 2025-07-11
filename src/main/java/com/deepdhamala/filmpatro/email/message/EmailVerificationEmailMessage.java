package com.deepdhamala.filmpatro.email.message;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCode;
import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCode;

public class EmailVerificationEmailMessage implements EmailMessage<EmailVerificationEmailMessage> {

    private final EmailVerificationCode emailVerificationCode;

    public EmailVerificationEmailMessage(EmailVerificationCode emailVerificationCode) {
        this.emailVerificationCode = emailVerificationCode;
    }

    @Override
    public String getTo() {
        return emailVerificationCode.getUser().getEmail();
    }

    @Override
    public String getSubject() {
        return "Verify Your Email - Film Patro";
    }

    @Override
    public String getHtmlContent() {
        String verificationLink = "http://localhost:8080/api/v1/auth/user/verify-email?verificationCode="
                + emailVerificationCode.getCode();

        return """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                    <h2 style="color: #2e6c80;">Verify Your Email</h2>
                    <p>Hello,</p>
                    <p>Thanks for registering with <strong>Film Patro</strong>!</p>
                    <p>Please confirm your email by clicking the button below:</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="display: inline-block; padding: 12px 24px; font-size: 16px; color: white; background-color: #28a745; text-decoration: none; border-radius: 6px;">
                            Verify Email
                        </a>
                    </div>
                    <p>This link will expire in <strong>30 minutes</strong>.</p>
                    <p>If you didn’t create an account, no action is required.</p>
                    <br/>
                    <p>Thanks,<br/>The Film Patro Team</p>
                </div>
            </body>
            </html>
            """.formatted(verificationLink);
    }
}
