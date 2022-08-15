package com.zero.zeroshop.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    private Long id;
    private String email;
    private String name;
    private String password;
    private LocalDate birth;
    private String phone;

}
