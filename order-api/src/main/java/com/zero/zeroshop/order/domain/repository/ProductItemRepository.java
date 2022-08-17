package com.zero.zeroshop.order.domain.repository;

import com.zero.zeroshop.order.domain.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

}
