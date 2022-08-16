package com.zero.zeroshop.user.controller;

import com.zero.zeroshop.domain.config.JwtAuthenticationProvider;
import com.zero.zeroshop.domain.common.UserVo;
import com.zero.zeroshop.user.domain.customer.CustomerDto;
import com.zero.zeroshop.user.domain.model.Customer;
import com.zero.zeroshop.user.exception.CustomerException;
import com.zero.zeroshop.user.exception.ErrorCode;
import com.zero.zeroshop.user.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
        UserVo userVo = provider.getUserVo(token);
        Customer customer = customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail()).orElseThrow(
                () -> new CustomerException(ErrorCode.NOT_FOUND_USER));
        return ResponseEntity.ok(CustomerDto.from(customer));
    }


}
