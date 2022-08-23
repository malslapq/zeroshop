package com.zero.zeroshop.order.application;


import com.zero.zeroshop.order.domain.model.Product;
import com.zero.zeroshop.order.domain.model.ProductItem;
import com.zero.zeroshop.order.domain.product.AddProductCartForm;
import com.zero.zeroshop.order.domain.redis.Cart;
import com.zero.zeroshop.order.exception.CustomException;
import com.zero.zeroshop.order.exception.ErrorCode;
import com.zero.zeroshop.order.service.CartService;
import com.zero.zeroshop.order.service.ProductSearchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartApplication {

    private final ProductSearchService productSearchService;
    private final CartService cartService;

    public Cart addCart(Long customerId, AddProductCartForm form) {

        Product product = productSearchService.getByProductId(form.getId());
        if (product == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_PRODUCT);
        }

        Cart cart = cartService.getCart(customerId);
        if (cart != null && !addAble(cart, product, form)) {
            throw new CustomException(ErrorCode.ITEM_COUNT_NOT_ENOUGH);
        }

        return cartService.addCart(customerId, form);
    }

    public Cart updateCart(Long customerId, Cart cart) {
        cartService.putCart(customerId, cart);
        return getCart(customerId);
    }

    public Cart getCart(Long customerId) {
        Cart cart = refreshCart(cartService.getCart(customerId));
        Cart returnCart = new Cart();
        returnCart.setCustomerId(customerId);
        returnCart.setProducts(cart.getProducts());
        returnCart.setMessages(cart.getMessages());
        cart.setMessages(new ArrayList<>());
        cartService.putCart(customerId, cart);
        return returnCart;
    }

    public void clearCart(Long customerId) {
        cartService.putCart(customerId, null);
    }

    private Cart refreshCart(Cart cart) {

        Map<Long, Product> productMap = productSearchService.getListByProductIds(
                cart.getProducts().stream().map(Cart.Product::getId).collect(Collectors.toList()))
            .stream().collect(Collectors.toMap(Product::getId, product -> product));

        for (int i = 0; i < cart.getProducts().size(); i++) {
            Cart.Product cartProduct = cart.getProducts().get(i);
            Product p = productMap.get(cartProduct.getId());

            if (p == null) {
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMessage(cartProduct.getName() + "상품이 삭제되었습니다.");
                continue;
            }

            Map<Long, ProductItem> productItemMap = p.getProductItems().stream()
                .collect(Collectors.toMap(ProductItem::getId, productItem -> productItem));

            List<String> tmpMessages = new ArrayList<>();
            for (int j = 0; j < cartProduct.getItems().size(); j++) {

                Cart.ProductItem cartProductItem = cartProduct.getItems().get(i);
                ProductItem productItem = productItemMap.get(cartProductItem.getId());
                if (productItem == null) {
                    cartProduct.getItems().remove(cartProductItem);
                    j--;
                    tmpMessages.add(cartProductItem.getName() + " 옵션이 삭제되었습니다.");
                    continue;
                }

                boolean isPriceChanged = false, isCountNotEnough = false;
                if (!cartProductItem.getPrice().equals(productItem.getPrice())) {
                    isPriceChanged = true;
                    cartProductItem.setPrice(productItem.getPrice());
                }
                if (cartProductItem.getCount() > (productItem.getCount())) {
                    isCountNotEnough = true;
                    cartProductItem.setCount(productItem.getCount());
                }

                if (isPriceChanged && isCountNotEnough) {
                    tmpMessages.add(
                        cartProductItem.getName() + " 가격과 수량이 변동되어 구매 가능한 최대 수량으로 변경되었습니다.");
                } else if (isPriceChanged) {
                    tmpMessages.add(cartProductItem.getName() + " 가격이 변동되었습니다.");
                } else if (isCountNotEnough) {
                    tmpMessages.add(
                        cartProductItem.getName() + " 수량이 변동되어 구매 가능한 최대 수량으로 변경되었습니다.");
                }
            }
            if (cartProduct.getItems().size() == 0) {
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMessage(cartProduct.getName() + "상품의 옵션이 존재하지 않아 구매가 불가능합니다.");
            } else if (tmpMessages.size() > 0) {
                StringBuilder builder = new StringBuilder();
                builder.append(cartProduct.getName()).append(" 상품의 변동 사항 : ");
                for (String message : tmpMessages) {
                    builder.append(message);
                    builder.append(", ");
                }
                cart.addMessage(builder.toString());
            }
        }
        cartService.putCart(cart.getCustomerId(), cart);
        return cart;
    }

    private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
        Cart.Product cartProduct = cart.getProducts().stream()
            .filter(p -> p.getId().equals(form.getId()))
            .findFirst().orElse(Cart.Product.builder().id(product.getId())
                .items(Collections.emptyList()).build());

        Map<Long, Integer> carItemCountMap = cartProduct.getItems().stream()
            .collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));

        Map<Long, Integer> currentItemCountMap = product.getProductItems().stream()
            .collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));

        return form.getItems().stream().noneMatch(
            formItem -> {
                Integer cartCount = carItemCountMap.get(formItem.getId());
                if (cartCount == null) {
                    cartCount = 0;
                }
                Integer currentCount = currentItemCountMap.get(formItem.getId());
                return formItem.getCount() + cartCount > currentCount;
            });
    }

}
