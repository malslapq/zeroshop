package com.zero.zeroshop.user.service;

import com.zero.zeroshop.user.domain.SignUpForm;
import com.zero.zeroshop.user.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignUpCustomerServiceTest {

    @Autowired
    private SignUpCustomerService service;

    @Test
    void signUp() {
        SignUpForm form = SignUpForm.builder()
                .name("name")
                .birth(LocalDate.now())
                .email("abc@gmail.com")
                .password("1")
                .phone("01011112222")
                .build();
        Customer customer = service.signUp(form);
        assertNotNull(customer.getId());
        assertNotNull(customer.getName());
        assertNotNull(customer.getEmail());
        assertNotNull(customer.getPhone());
        assertNotNull(customer.getPassword());
        assertNotNull(customer.getBirth());
    }
}