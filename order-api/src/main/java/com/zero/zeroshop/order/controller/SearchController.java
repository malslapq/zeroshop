package com.zero.zeroshop.order.controller;

import com.zero.zeroshop.order.domain.product.ProductDto;
import com.zero.zeroshop.order.service.ProductSearchService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductSearchService productSearchService;

    @GetMapping("/product")
    public ResponseEntity<List<ProductDto>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(productSearchService.searchByName(name).stream()
            .map(ProductDto::withoutItemsfrom).collect(Collectors.toList()));
    }

    @GetMapping("/product/detail")
    public ResponseEntity<ProductDto> getDetail(@RequestParam Long productId) {
        return ResponseEntity.ok(ProductDto.from(productSearchService.getByProductId(productId)));
    }


}
