package com.zero.zeroshop.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductItemForm {

    private Long productId;
    private String name;
    private Integer price;
    private Integer count;

}
