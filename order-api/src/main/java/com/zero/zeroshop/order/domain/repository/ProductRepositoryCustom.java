package com.zero.zeroshop.order.domain.repository;

import com.zero.zeroshop.order.domain.model.Product;
import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> searchByName(String name);

}
