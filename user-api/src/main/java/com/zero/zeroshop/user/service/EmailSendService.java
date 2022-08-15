package com.zero.zeroshop.user.service;

import com.zero.zeroshop.user.client.MailgunClient;
import com.zero.zeroshop.user.client.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {

    private final MailgunClient mailgunClient;

    public void sendMail(SendMailForm form) {
        mailgunClient.sendEmail(form);
    }

}
