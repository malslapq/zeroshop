package com.zero.zeroshop.order.service;

import com.zero.zeroshop.order.domain.model.AddProductItemForm;
import com.zero.zeroshop.order.domain.model.Product;
import com.zero.zeroshop.order.domain.model.ProductItem;
import com.zero.zeroshop.order.domain.repository.ProductItemRepository;
import com.zero.zeroshop.order.domain.repository.ProductRepository;
import com.zero.zeroshop.order.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zero.zeroshop.order.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ProductItemService {

    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;

    @Transactional
    public Product addProductItem(Long sellerId, AddProductItemForm form) {
        Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
            .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
        if (product.getProductItems().stream()
            .anyMatch(item -> item.getName().equals(form.getName()))) {
            throw new CustomException(SAME_ITEM_NAME);
        }
        ProductItem productItem = ProductItem.of(sellerId, form);
        product.getProductItems().add(productItem);
        return product;
    }


}
