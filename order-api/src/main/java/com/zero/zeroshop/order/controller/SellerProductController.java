package com.zero.zeroshop.order.controller;

import com.zero.zeroshop.domain.config.JwtAuthenticationProvider;
import com.zero.zeroshop.order.domain.model.AddProductItemForm;
import com.zero.zeroshop.order.domain.model.ProductDto;
import com.zero.zeroshop.order.domain.product.AddProductForm;
import com.zero.zeroshop.order.service.ProductItemService;
import com.zero.zeroshop.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;
    private final ProductItemService productItemService;
    private final JwtAuthenticationProvider provider;


    @PostMapping
    public ResponseEntity<?> addProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token,
        @RequestBody
        AddProductForm form) {
        return ResponseEntity.ok(
            ProductDto.from(productService.addProduct(provider.getUserVo(token).getId(), form)));
    }

    @PostMapping("/item")
    public ResponseEntity<ProductDto> addProductItem(
        @RequestHeader(name = "X-AUTH-TOKEN") String token,
        @RequestBody AddProductItemForm form) {
        return ResponseEntity.ok(ProductDto.from(
            productItemService.addProductItem(provider.getUserVo(token).getId(), form)));
    }

}
