package com.zero.zeroshop.order.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.zeroshop.order.domain.model.Product;
import com.zero.zeroshop.order.domain.model.QProduct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> searchByName(String name) {
        String search = "%"+name+"%";
        QProduct product = QProduct.product;
        return queryFactory.selectFrom(product)
            .where(product.name.like(search))
            .fetch();
    }
}
