package com.zero.zeroshop.user.controller;

import com.zero.zeroshop.user.application.SignUpApplication;
import com.zero.zeroshop.user.domain.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/signup")
@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpApplication signUpApplication;

    @PostMapping("/customer")
    public ResponseEntity<String> customerSighUp(@RequestBody SignUpForm form) {
        return ResponseEntity.ok(signUpApplication.customerSignUp(form));
    }

    @PutMapping("/customer/verify")
    public ResponseEntity<String> verifyCustomer(String email, String code) {
        signUpApplication.customerVerify(email, code);
        return ResponseEntity.ok("인증 완료");
    }

    @PostMapping("/seller")
    public ResponseEntity<String> sellerSighUp(@RequestBody SignUpForm form) {
        return ResponseEntity.ok(signUpApplication.sellerSignUp(form));
    }

    @PutMapping("/seller/verify")
    public ResponseEntity<String> verifySeller(String email, String code) {
        signUpApplication.sellerVerify(email, code);
        return ResponseEntity.ok("인증 완료");
    }

}
