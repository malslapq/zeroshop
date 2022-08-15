package com.zero.zeroshop.user.service;

import com.zero.zeroshop.user.domain.SignUpForm;
import com.zero.zeroshop.user.domain.model.Customer;
import com.zero.zeroshop.user.domain.repository.CustomerRepository;
import com.zero.zeroshop.user.exception.CustomerException;
import com.zero.zeroshop.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

    private final CustomerRepository customerRepository;

    public Customer signUp(SignUpForm form) {
        return customerRepository.save(Customer.from(form));
    }

    public boolean isEmailExist(String email) {
        return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT)).isPresent();
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        Customer customer =
                customerRepository.findByEmail(email).orElseThrow(
                        () -> new CustomerException(ErrorCode.NOT_FOUND_USER_FROM_EMAIL));
        if (customer.isVerify()) {
            throw new CustomerException(ErrorCode.ALREADY_VERIFY);
        }
        if (!customer.getVerificationCode().equals(code)) {
            throw new CustomerException(ErrorCode.WRONG_VERIFICATION);
        }
        if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomerException(ErrorCode.EXPIRE_CODE);
        }
        customer.verificationCompleted();
    }

    @Transactional
    public LocalDateTime ChangeCustomerValidateEmail(Long CustomerId, String verificationCode) {
        Customer customer =
                customerRepository.findById(CustomerId).orElseThrow(
                        () -> new CustomerException(ErrorCode.NOT_FOUND_USER_FROM_EMAIL));
        customer.changeVerifications(verificationCode);
        return customer.getVerifyExpiredAt();
    }

}
