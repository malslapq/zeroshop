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

    @PostMapping
    public ResponseEntity<String> customerSighUp(@RequestBody SignUpForm form) {
        return ResponseEntity.ok(signUpApplication.customerSignUp(form));
    }

    @PutMapping("/verify/customer")
    public ResponseEntity<String> verifyCustomer(String email, String code) {
        signUpApplication.customerVerify(email, code);
        return ResponseEntity.ok("인증 완료");
    }

}
