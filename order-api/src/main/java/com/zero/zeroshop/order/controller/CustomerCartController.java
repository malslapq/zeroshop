package com.zero.zeroshop.order.controller;

import com.zero.zeroshop.domain.config.JwtAuthenticationProvider;
import com.zero.zeroshop.order.application.CartApplication;
import com.zero.zeroshop.order.domain.product.AddProductCartForm;
import com.zero.zeroshop.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer/cart")
public class CustomerCartController {

    private final CartApplication cartApplication;
    private final JwtAuthenticationProvider provider;

    @PostMapping
    public ResponseEntity<Cart> addCart(
        @RequestHeader(name = "X-AUTH-TOKEN") String token,
        @RequestBody AddProductCartForm form) {
        return ResponseEntity.ok(cartApplication.addCart(provider.getUserVo(token).getId(), form));
    }

}
