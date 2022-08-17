package com.zero.zeroshop.order.service;

import com.zero.zeroshop.order.domain.model.Product;
import com.zero.zeroshop.order.domain.product.AddProductForm;
import com.zero.zeroshop.order.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product addProduct(Long sellerId, AddProductForm form) {
        return productRepository.save(Product.of(sellerId, form));
    }

}
