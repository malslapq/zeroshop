package com.zero.zeroshop.order.domain.model;

import com.zero.zeroshop.order.domain.product.AddProductForm;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@AuditOverride(forClass = BaseEntity.class)
@Audited
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductItem> productItems = new ArrayList<>();

    public static Product of(Long sellerId,AddProductForm form) {
        return Product.builder()
            .sellerId(sellerId)
            .name(form.getName())
            .description(form.getDescription())
            .productItems(form.getItems().stream()
                .map(piForm -> ProductItem.of(sellerId, piForm)).collect(Collectors.toList()))
            .build();

    }

}
