package com.zero.zeroshop.user.client.mailgun;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SendMailForm {

    private String from;
    private String to;
    private String subject;
    private String text;

}
