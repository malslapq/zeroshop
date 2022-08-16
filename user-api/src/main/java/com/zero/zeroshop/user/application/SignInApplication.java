package com.zero.zeroshop.user.application;

import com.zero.zeroshop.domain.common.UserType;
import com.zero.zeroshop.domain.config.JwtAuthenticationProvider;
import com.zero.zeroshop.user.domain.SignInForm;
import com.zero.zeroshop.user.domain.model.Customer;
import com.zero.zeroshop.user.domain.model.Seller;
import com.zero.zeroshop.user.exception.CustomException;
import com.zero.zeroshop.user.exception.ErrorCode;
import com.zero.zeroshop.user.service.customer.CustomerService;
import com.zero.zeroshop.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final SellerService sellerService;
    private final JwtAuthenticationProvider provider;


    public String customerLoginToken(SignInForm form) {
        // 1. 로그인 가능 여부
        Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword()).orElseThrow(
                () -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));
        // 2. 토큰 발행
        return provider.createToken(customer.getEmail(), customer.getId(), UserType.CUSTOMER);
    }

    public String sellerLoginToken(SignInForm form) {
        // 1. 로그인 가능 여부
        Seller seller =
                sellerService.findValidSeller(form.getEmail(), form.getPassword()).orElseThrow(
                        () -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));
        // 2. 토큰 발행
        return provider.createToken(seller.getEmail(), seller.getId(), UserType.SELLER);
    }

}
