package com.deepdhamala.filmpatro.email.message;

import com.deepdhamala.filmpatro.auth.oneTimeCode.otp.OtpEntity;

public class OtpEmailMessage implements EmailMessage<OtpEmailMessage>{

    private final OtpEntity otp;

    public OtpEmailMessage(OtpEntity otp) {
        this.otp = otp;
    }

    @Override
    public String getTo() {
        return otp.getUser().getEmail();
    }

    @Override
    public String getSubject() {
        return "Your One-Time Password (OTP) - Film Patro";
    }

    @Override
    public String getHtmlContent() {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);">
                    <h2 style="color: #2e6c80;">Your OTP Code</h2>
                    <p>Hello,</p>
                    <p>To complete your action, please use the following One-Time Password (OTP):</p>
                    <div style="font-size: 24px; font-weight: bold; color: #333; background-color: #e9ecef; padding: 10px 20px; border-radius: 5px; text-align: center;">
                        %s
                    </div>
                    <p style="margin-top: 20px;">This code will expire in <strong>5 minutes</strong>.</p>
                    <p>If you did not request this code, please ignore this email.</p>
                    <br/>
                    <p>Thanks,<br/>The Film Patro Team</p>
                </div>
            </body>
            </html>
            """.formatted(otp.getOtp());
    }
}
