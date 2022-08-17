package com.zero.zeroshop.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zero.zeroshop.order.domain.model.AddProductItemForm;
import com.zero.zeroshop.order.domain.model.Product;
import com.zero.zeroshop.order.domain.model.ProductItem;
import com.zero.zeroshop.order.domain.product.AddProductForm;
import com.zero.zeroshop.order.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("product add test")
    @Test
    void ADD_PRODUCT_TEST() {
        // given
        Long sellerId = 1L;
        String name = "나이키 에어포스";
        String description = "신발";
        int itemCount = 5;
        AddProductForm form = makeProductForm(name, description, itemCount);
        int price = 10000;
        int count = 0;

        // when
        Product product = productService.addProduct(sellerId, form);
        Product result = productRepository.findWithProductItemsById(product.getId()).get();

        // then
        assertEquals(result.getSellerId(), sellerId);
        assertEquals(result.getName(), name);
        assertEquals(result.getDescription(), description);
        assertEquals(result.getProductItems().size(), itemCount);
        for (ProductItem item : result.getProductItems()) {
            assertEquals(item.getName(), name + count);
            assertEquals(item.getPrice(), price);
            assertEquals(item.getCount(), 1);
            count++;
        }
    }

    private static AddProductForm makeProductForm(String name, String description, int itemCount) {
        List<AddProductItemForm> itemForms = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            itemForms.add(makeProductItemForm(name + i));
        }
        return AddProductForm.builder()
            .name(name)
            .description(description)
            .items(itemForms)
            .build();
    }

    private static AddProductItemForm makeProductItemForm(String name) {
        return AddProductItemForm.builder()
            .productId(null)
            .name(name)
            .price(10000)
            .count(1)
            .build();
    }

}