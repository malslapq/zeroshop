package com.zero.zeroshop.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zero.zeroshop.order.domain.model.Product;
import com.zero.zeroshop.order.domain.model.ProductItem;
import com.zero.zeroshop.order.domain.product.AddProductForm;
import com.zero.zeroshop.order.domain.product.AddProductItemForm;
import com.zero.zeroshop.order.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductSearchServiceTest {

    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void initData() {
        productRepository.save(
            Product.of(
                1L,
                AddProductForm.builder()
                    .name("츄파츕스")
                    .description("사탕")
                    .items(List.of(AddProductItemForm.builder()
                        .name("딸기우유맛")
                        .price(300)
                        .count(10)
                        .build()))
                    .build()));
        productRepository.save(
            Product.of(
                1L,
                AddProductForm.builder()
                    .name("옥동자")
                    .description("아이스크림")
                    .items(List.of(AddProductItemForm.builder()
                        .name("오레오맛")
                        .price(800)
                        .count(100)
                        .build()))
                    .build()));
    }

    @AfterEach
    void deleteData() {
        productRepository.deleteAll();
    }

    @DisplayName("검색 쿼리 사용 Product 가져오기")
    @Test
    void searchByName() {
        // given
        String name = "츄";

        // when
        List<Product> result = productSearchService.searchByName(name);
        Product product = result.get(0);

        // then
        assertEquals(product.getName(), "츄파츕스");
        assertEquals(product.getDescription(), "사탕");

    }

    @DisplayName("ProductId로 상세정보까지 가져오기")
    @Test
    void getByProductId() {
        // given
        Long id = 1L;

        // when
        Product product = productSearchService.getByProductId(id);
        ProductItem productItem = product.getProductItems().get(0);

        // then
        assertEquals(product.getName(), "츄파츕스");
        assertEquals(product.getDescription(), "사탕");
        assertEquals(productItem.getName(), "딸기우유맛");
        assertEquals(productItem.getPrice(), 300);
        assertEquals(productItem.getCount(), 10);

    }

    @DisplayName("ProductId 여러개로 Products 가져오기")
    @Test
    void getListByProductIds() {
        // given
        List<Long> ids = new ArrayList<>();
        List<Product> productList = productRepository.findAll();
        for (Product p : productList) {
            ids.add(p.getId());
        }

        // when
        List<Product> products = productSearchService.getListByProductIds(ids);

        // then
        assertEquals(products.get(0).getName(), "츄파츕스");
        assertEquals(products.get(0).getDescription(), "사탕");
        assertEquals(products.get(1).getName(), "옥동자");
        assertEquals(products.get(1).getDescription(), "아이스크림");
    }
}