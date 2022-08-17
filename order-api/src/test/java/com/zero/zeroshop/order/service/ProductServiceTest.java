package com.zero.zeroshop.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.zero.zeroshop.order.domain.model.Product;
import com.zero.zeroshop.order.domain.model.ProductItem;
import com.zero.zeroshop.order.domain.product.AddProductForm;
import com.zero.zeroshop.order.domain.product.AddProductItemForm;
import com.zero.zeroshop.order.domain.product.ProductDto;
import com.zero.zeroshop.order.domain.product.UpdateProductForm;
import com.zero.zeroshop.order.domain.product.UpdateProductItemForm;
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
    private ProductItemService productItemService;
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
        int price = 10000;
        AddProductForm form = makeProductForm(name, description, itemCount, price);
        int count = 0;

        // when
        Product product = productService.addProduct(sellerId, form);
        Product result = productRepository.findWithProductItemsById(product.getId()).get();
        System.out.println(ProductDto.from(result));
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

    @DisplayName("Product Modify Test")
    @Test
    void PRODUCT_MODIFY_TEST() {
        // given
        Long sellerId = 1L;
        String name = "나이키 에어포스";
        String description = "신발";
        int itemCount = 1;
        int price = 10000;
        AddProductForm form = makeProductForm(name, description, itemCount, price);
        productService.addProduct(sellerId, form);

        int changePrice = 100000;
        int changeItemCount = 2;
        String changeName = "나이키 조던";
        String changeDescription = "한정판 신발";
        UpdateProductItemForm itemsForm = UpdateProductItemForm.builder()
            .id(1L)
            .name(changeName)
            .price(changePrice)
            .count(changeItemCount)
            .build();
        UpdateProductForm productForm = UpdateProductForm.builder()
            .id(1L)
            .name(changeName)
            .description(changeDescription)
            .items(List.of(itemsForm))
            .build();

        // when
        Product result = productService.updateProduct(sellerId, productForm);

        // then
        assertEquals(result.getName(), changeName);
        assertEquals(result.getDescription(), changeDescription);
        assertEquals(result.getProductItems().get(0).getName(), changeName);
        assertEquals(result.getProductItems().get(0).getPrice(), changePrice);
        assertEquals(result.getProductItems().get(0).getCount(), changeItemCount);

    }

    @DisplayName("ProductItem Delete Test")
    @Test
    void PRODUCT_ITEM_DELETE_TEST(){
        // given
        Long sellerId = 1L;
        String name = "나이키 에어포스";
        String description = "신발";
        int itemCount = 5;
        int price = 10000;
        AddProductForm form = makeProductForm(name, description, itemCount, price);
        int oldSize = productService.addProduct(sellerId, form).getProductItems().size();

        // when
        productItemService.deleteProductItem(sellerId, 1L);
        Product product = productRepository.findBySellerIdAndId(sellerId, 1L).get();

        // then
        assertEquals(oldSize-1, product.getProductItems().size());
    }

    @DisplayName("Product Delete Test")
    @Test
    void PRODUCT_DELETE_TEST(){
        // given
        Long sellerId = 1L;
        String name = "나이키 에어포스";
        String description = "신발";
        int itemCount = 5;
        int price = 10000;
        AddProductForm form = makeProductForm(name, description, itemCount, price);
        productService.addProduct(sellerId, form);

        // when
        productService.deleteProduct(sellerId, 1L);
        Product result = productRepository.findById(1L).orElse(null);

        // then
        assertNull(result);
    }

    private static AddProductForm makeProductForm(String name, String description, int itemCount,
        int price) {
        List<AddProductItemForm> itemForms = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            itemForms.add(makeProductItemForm(name + i, price));
        }
        return AddProductForm.builder()
            .name(name)
            .description(description)
            .items(itemForms)
            .build();
    }

    private static AddProductItemForm makeProductItemForm(String name, int price) {
        return AddProductItemForm.builder()
            .productId(null)
            .name(name)
            .price(price)
            .count(1)
            .build();
    }

}