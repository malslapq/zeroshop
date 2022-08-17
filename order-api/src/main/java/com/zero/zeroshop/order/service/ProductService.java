package com.zero.zeroshop.order.service;

import com.zero.zeroshop.order.domain.model.Product;
import com.zero.zeroshop.order.domain.product.AddProductForm;
import com.zero.zeroshop.order.domain.product.UpdateProductForm;
import com.zero.zeroshop.order.domain.repository.ProductRepository;
import com.zero.zeroshop.order.exception.CustomException;
import com.zero.zeroshop.order.exception.ErrorCode;
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

    @Transactional
    public Product updateProduct(Long sellerId, UpdateProductForm form) {
        Product product = productRepository.findBySellerIdAndId(sellerId, form.getId())
            .orElseThrow(() -> new CustomException(
                ErrorCode.NOT_FOUND_PRODUCT));
        product.update(form);
        return product;
    }

    @Transactional
    public void deleteProduct(Long sellerId, Long productId) {
        Product product = productRepository.findBySellerIdAndId(sellerId, productId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        productRepository.delete(product);
    }

}
