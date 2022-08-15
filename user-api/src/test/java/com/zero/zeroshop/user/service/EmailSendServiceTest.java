package com.zero.zeroshop.user.service;

import com.zero.zeroshop.user.client.MailgunClient;
import com.zero.zeroshop.user.client.mailgun.SendMailForm;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailSendServiceTest {

    @Autowired
    private MailgunClient mailgunClient;

    @Test
    void sendMail() {

        SendMailForm form = SendMailForm.builder()
                .from("malslapq@gmail.com")
                .to("malslapq@naver.com")
                .subject("zero Test")
                .text("Test Mail")
                .build();
        Response response = mailgunClient.sendEmail(form);
        assertEquals(response.status(), 200);
    }
}