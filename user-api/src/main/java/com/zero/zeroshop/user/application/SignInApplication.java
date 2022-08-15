package com.zero.zeroshop.user.application;

import com.zero.zeroshop.domain.config.JwtAuthenticationProvider;
import com.zero.zeroshop.domain.domain.common.UserType;
import com.zero.zeroshop.user.domain.SignInForm;
import com.zero.zeroshop.user.domain.model.Customer;
import com.zero.zeroshop.user.exception.CustomerException;
import com.zero.zeroshop.user.exception.ErrorCode;
import com.zero.zeroshop.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final JwtAuthenticationProvider provider;


    public String customerLoginToken(SignInForm form) {
        // 1. 로그인 가능 여부
        Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword()).orElseThrow(
                () -> new CustomerException(ErrorCode.LOGIN_CHECK_FAIL));
        // 2. 토큰 발행


        // 3. 토큰 응답
        return provider.createToken(customer.getEmail(), customer.getId(), UserType.CUSTOMER);
    }

}
