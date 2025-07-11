package com.deepdhamala.filmpatro.email;

import com.deepdhamala.filmpatro.email.message.EmailMessage;

public interface EmailService {

    void sendEmail(EmailMessage<?> emailMessage);

 }