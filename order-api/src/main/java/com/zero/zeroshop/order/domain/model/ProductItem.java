package com.zero.zeroshop.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class ProductItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellerId;
    @Audited
    private String name;
    @Audited
    private Integer price;

    private Integer count;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    public static ProductItem of(Long sellerId, AddProductItemForm form) {
        return ProductItem.builder()
            .sellerId(sellerId)
            .name(form.getName())
            .price(form.getPrice())
            .count(form.getCount())
            .build();
    }

}
