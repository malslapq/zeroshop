package com.zero.zeroshop.order.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zero.zeroshop.order.domain.model.Product;
import com.zero.zeroshop.order.domain.model.ProductItem;
import com.zero.zeroshop.order.domain.product.AddProductCartForm;
import com.zero.zeroshop.order.domain.product.AddProductForm;
import com.zero.zeroshop.order.domain.product.AddProductItemForm;
import com.zero.zeroshop.order.domain.product.UpdateProductForm;
import com.zero.zeroshop.order.domain.product.UpdateProductItemForm;
import com.zero.zeroshop.order.domain.redis.Cart;
import com.zero.zeroshop.order.domain.repository.ProductRepository;
import com.zero.zeroshop.order.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CartApplicationTest {

    @Autowired
    private CartApplication cartApplication;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("장바구니 추가 테스트")
    @Test
    void ADD_TEST() {
        // given
        Long sellerId = 1L;
        String name = "나이키 에어포스";
        String description = "신발";
        int price = 10000;
        Product product = addProduct();
        Long customerId = 1L;
        cartApplication.clearCart(customerId);
        AddProductCartForm addProductCartForm = makeAddCartForm(product);
        cartApplication.addCart(customerId, addProductCartForm);

        // when
        Cart result = cartApplication.getCart(customerId);

        // then
        assertEquals(result.getProducts().get(0).getSellerId(), sellerId);
        assertEquals(result.getProducts().get(0).getName(), name);
        assertEquals(result.getProducts().get(0).getDescription(), description);
        assertEquals(result.getProducts().get(0).getItems().get(0).getPrice(), price);
        assertEquals(result.getProducts().get(0).getItems().get(0).getCount(), 3);
    }

    @DisplayName("장바구니 확인시 변경 대응 테스트")
    @Test
    void CART_MODIFY_TEST() {
        // given
        Product product = addProduct();
        Long customerId = 1L;
        cartApplication.clearCart(customerId);
        AddProductCartForm addProductCartForm = makeAddCartForm(product);
        cartApplication.addCart(customerId, addProductCartForm);
        ProductItem productItem = product.getProductItems().get(0);

        // when
        cartApplication.getCart(customerId);
        productItem.changeCount(3);
        Product updateProduct1 = modifyProduct(customerId, product);
        Cart result1 = cartApplication.getCart(customerId);
        updateProduct1.getProductItems().get(0).changePrice(5000);
        Product updateProduct2 = modifyProduct(customerId, updateProduct1);
        Cart result2 = cartApplication.getCart(customerId);
        updateProduct2.getProductItems().get(0).changeCount(2);
        updateProduct2.getProductItems().get(0).changePrice(1000);
        modifyProduct(customerId, updateProduct2);
        Cart result3 = cartApplication.getCart(customerId);
        updateProduct2.resetItems();
        productRepository.save(updateProduct2);
        Cart result4 = cartApplication.getCart(customerId);

        // then
        assertEquals(result1.getProducts().get(0).getItems().get(0).getCount(), 3);
        assertEquals(result2.getProducts().get(0).getItems().get(0).getPrice(), 5000);
        assertEquals(result3.getProducts().get(0).getItems().get(0).getCount(), 2);
        assertEquals(result3.getProducts().get(0).getItems().get(0).getPrice(), 1000);
        assertEquals(result4.getProducts().size(), 0);

    }

    @DisplayName("장바구니 업데이트 테스트")
    @Test
    void UPDATE_CART_TEST() {
        // given
        Product product = addProduct();
        Long customerId = 1L;
        cartApplication.clearCart(customerId);
        AddProductCartForm addProductCartForm = makeAddCartForm(product);
        Cart cart = cartApplication.addCart(customerId, addProductCartForm);
        cart.getProducts().get(0).getItems().get(0).setCount(2);

        // when
        Cart updateCart = cartApplication.updateCart(customerId, cart);

        // then
        assertEquals(updateCart.getProducts().get(0).getItems().get(0).getCount(), 2);
    }


    AddProductCartForm makeAddCartForm(Product product) {
        AddProductCartForm.ProductItem productItem = AddProductCartForm.ProductItem.builder()
            .id(product.getProductItems().get(0).getId())
            .name(product.getProductItems().get(0).getName())
            .count(4)
            .price(product.getProductItems().get(0).getPrice())
            .build();
        return AddProductCartForm.builder()
            .id(product.getId())
            .sellerId(product.getSellerId())
            .name(product.getName())
            .description(product.getDescription())
            .items(List.of(productItem))
            .build();
    }

    Product addProduct() {
        Long sellerId = 1L;
        String name = "나이키 에어포스";
        String description = "신발";
        int itemCount = 10;
        int price = 10000;
        AddProductForm form = makeProductForm(name, description, itemCount, price);
        return productService.addProduct(sellerId, form);
    }

    Product modifyProduct(Long customerId, Product product) {
        UpdateProductItemForm itemForm = UpdateProductItemForm.builder()
            .id(1L)
            .name(product.getName() + 0)
            .price(product.getProductItems().get(0).getPrice())
            .count(product.getProductItems().get(0).getCount())
            .build();
        UpdateProductForm form = UpdateProductForm.builder()
            .id(1L)
            .name(product.getName())
            .description(product.getDescription())
            .items(List.of(itemForm))
            .build();
        return productService.updateProduct(customerId, form);
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
            .name(name)
            .price(price)
            .count(10)
            .build();
    }

}