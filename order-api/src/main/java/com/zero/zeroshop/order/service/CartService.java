package com.zero.zeroshop.order.service;

import com.zero.zeroshop.order.client.RedisClient;
import com.zero.zeroshop.order.domain.product.AddProductCartForm;
import com.zero.zeroshop.order.domain.redis.Cart;
import com.zero.zeroshop.order.domain.redis.Cart.ProductItem;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {

    private final RedisClient redisClient;

    public Cart putCart(Long customerId, Cart cart) {
        redisClient.put(customerId, cart);
        return cart;
    }

    public Cart getCart(Long customerId) {
        Cart cart =redisClient.get(customerId, Cart.class);
        return cart != null ? cart : new Cart();
    }

    public Cart addCart(Long customerId, AddProductCartForm form) {
        Cart cart = redisClient.get(customerId, Cart.class);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
        }

        Optional<Cart.Product> productOptional = cart.getProducts().stream()
            .filter(product1 -> product1.getId().equals(form.getId()))
            .findFirst();

        if (productOptional.isPresent()) {
            Cart.Product redisProduct = productOptional.get();
            List<Cart.ProductItem> items = form.getItems()
                .stream()
                .map(Cart.ProductItem::from)
                .collect(Collectors.toList());

            Map<Long, Cart.ProductItem> redisItemMap = redisProduct.getItems()
                .stream().collect(Collectors.toMap(ProductItem::getId, it -> it));
            if (redisProduct.getName().equals(form.getName())) {
                cart.addMessage(redisProduct.getName() + "의 정보가 변경되었습니다. 확인 해주시길 바랍니다. ");
            }
            for (Cart.ProductItem item : items) {
                Cart.ProductItem redisItem = redisItemMap.get(item.getId());

                // 아예 없음 추가 해주면 끝
                if (redisItem == null) {
                    redisProduct.getItems().add(item);

                } else {
                    if (!redisItem.getPrice().equals(item.getPrice())) {
                        cart.addMessage(redisProduct.getName() + item.getName() + "의 가격이 변경되었습니다. 확인 해주시길 바랍니다. ");
                    }
                    redisItem.setCount(redisItem.getCount() + item.getCount());
                }
            }
        } else {
            Cart.Product product = Cart.Product.from(form);
            cart.getProducts().add(product);
        }
        redisClient.put(customerId, cart);
        return cart;

    }

}