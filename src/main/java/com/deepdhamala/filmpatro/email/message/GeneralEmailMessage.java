package com.deepdhamala.filmpatro.email.message;

public class GeneralEmailMessage implements EmailMessage<GeneralEmailMessage> {

    private final String to;
    private final String subject;
    private final String body;

    public GeneralEmailMessage(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getHtmlContent() {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                    <p style="font-size: 16px; color: #333;">
                        %s
                    </p>
                    <br/>
                    <p style="font-size: 14px; color: #888;">Sent via Film Patro Email System</p>
                </div>
            </body>
            </html>
            """.formatted(body.replace("\n", "<br/>"));
    }
}
