package com.zero.zeroshop.user.controller;

import com.zero.zeroshop.domain.common.UserVo;
import com.zero.zeroshop.domain.config.JwtAuthenticationProvider;
import com.zero.zeroshop.user.domain.customer.ChangeBalanceForm;
import com.zero.zeroshop.user.domain.customer.CustomerDto;
import com.zero.zeroshop.user.domain.model.Customer;
import com.zero.zeroshop.user.exception.CustomException;
import com.zero.zeroshop.user.exception.ErrorCode;
import com.zero.zeroshop.user.service.customer.CustomerBalanceService;
import com.zero.zeroshop.user.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;
    private final CustomerBalanceService customerBalanceService;

    @GetMapping("/getinfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
        UserVo userVo = provider.getUserVo(token);
        Customer customer = customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return ResponseEntity.ok(CustomerDto.from(customer));
    }

    @PostMapping("/balance")
    public ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                 @RequestBody ChangeBalanceForm form) {
        UserVo userVo = provider.getUserVo(token);
        return ResponseEntity.ok(customerBalanceService.changeBalance(userVo.getId(), form).getCurrentMoney());
    }


}
