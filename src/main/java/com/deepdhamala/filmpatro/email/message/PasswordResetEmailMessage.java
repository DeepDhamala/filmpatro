package com.deepdhamala.filmpatro.email.message;

import com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode.ForgetPasswordRecoveryCode;

public class PasswordResetEmailMessage implements EmailMessage<PasswordResetEmailMessage> {

    private final ForgetPasswordRecoveryCode forgetPasswordResetCode;

    public PasswordResetEmailMessage(ForgetPasswordRecoveryCode forgetPasswordResetCode) {
        this.forgetPasswordResetCode = forgetPasswordResetCode;
    }

    @Override
    public String getTo() {
        return forgetPasswordResetCode.getUser().getEmail();
    }

    @Override
    public String getSubject() {
        return "Password Reset Request";
    }

    @Override
    public String getHtmlContent() {
        String resetLink = "https://localhost:8080/?forgetPasswordRecoverytoken=" + forgetPasswordResetCode.getCode();
        return """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);">
                    <h2 style="color: #2e6c80;">Password Reset Request</h2>
                    <p>Hello,</p>
                    <p>We received a request to reset your password. Click the button below to reset it:</p>
                    <div style="text-align: center; margin: 20px 0;">
                        <a href="%s" style="display: inline-block; padding: 12px 24px; font-size: 16px; color: white; background-color: #007BFF; text-decoration: none; border-radius: 5px;">
                            Reset Password
                        </a>
                    </div>
                    <p>If you didn't request this, you can safely ignore this email.</p>
                    <p>Thanks,<br/>The Film Patro Team</p>
                </div>
            </body>
            </html>
        """.formatted(resetLink);
    }
}
