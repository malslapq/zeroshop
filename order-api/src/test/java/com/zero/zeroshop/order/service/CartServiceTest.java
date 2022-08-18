package com.zero.zeroshop.order.service;

import com.zero.zeroshop.order.domain.product.AddProductCartForm;
import com.zero.zeroshop.order.domain.product.AddProductCartForm.ProductItem;
import com.zero.zeroshop.order.domain.product.AddProductForm;
import com.zero.zeroshop.order.domain.product.AddProductItemForm;
import com.zero.zeroshop.order.domain.redis.Cart;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;

    @BeforeEach
    void init() {
        Long sellerId = 1L;
        AddProductForm productForm = AddProductForm.builder()
            .name("딸기")
            .description("과일")
            .items(List.of(AddProductItemForm.builder()
                .name("설향딸기")
                .price(10000)
                .count(10)
                .build()))
            .build();
        productService.addProduct(sellerId, productForm);
    }


    @DisplayName("장바구니 추가 테스트")
    @Test
    void addCart() {
        // given
        Long customerId = 1L;
        Long sellerId = 1L;
        Long productId = 1L;
        String productName = "딸기";
        String description = "과일";
        Long productItemId = 1L;
        String productItemName = "설향딸기";
        Integer price = 10000;

        ProductItem productItem = ProductItem.builder()
            .id(productItemId)
            .name(productItemName)
            .price(price)
            .count(3)
            .build();
        AddProductCartForm form = AddProductCartForm.builder()
            .id(productId)
            .sellerId(sellerId)
            .name(productName)
            .description(description)
            .items(List.of(productItem))
            .build();

        // when
        cartService.addCart(customerId, form);
        Cart result = cartService.getCart(customerId);

        // then
        assertEquals(result.getProducts().get(0).getName(), productName);
        assertEquals(result.getProducts().get(0).getDescription(), description);
        assertEquals(result.getProducts().get(0).getSellerId(), sellerId);
        assertEquals(result.getProducts().get(0).getItems().get(0).getId(), productItemId);
        assertEquals(result.getProducts().get(0).getItems().get(0).getName(), productItemName);
        assertEquals(result.getProducts().get(0).getItems().get(0).getPrice(), price);
        assertEquals(result.getProducts().get(0).getItems().get(0).getCount(), 3);
    }
}