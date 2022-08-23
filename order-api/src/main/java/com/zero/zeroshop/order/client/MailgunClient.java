package com.zero.zeroshop.order.client;

import com.zero.zeroshop.order.client.mailgun.SendMailForm;
import feign.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

    @PostMapping("sandboxe7642021ce934455b360b7bec8c6b4e9.mailgun.org/messages")
    Response sendEmail(@SpringQueryMap SendMailForm form);

}
