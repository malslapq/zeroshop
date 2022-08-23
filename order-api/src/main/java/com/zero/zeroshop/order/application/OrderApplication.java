package com.zero.zeroshop.order.application;

import com.zero.zeroshop.order.client.MailgunClient;
import com.zero.zeroshop.order.client.UserClient;
import com.zero.zeroshop.order.client.mailgun.SendMailForm;
import com.zero.zeroshop.order.client.user.ChangeBalanceForm;
import com.zero.zeroshop.order.client.user.CustomerDto;
import com.zero.zeroshop.order.domain.model.ProductItem;
import com.zero.zeroshop.order.domain.redis.Cart;
import com.zero.zeroshop.order.exception.CustomException;
import com.zero.zeroshop.order.exception.ErrorCode;
import com.zero.zeroshop.order.service.ProductItemService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApplication {

    private final CartApplication cartApplication;
    private final UserClient userClient;
    private final ProductItemService productItemService;
    private final MailgunClient mailgunClient;

    @Transactional
    public void order(String token, Cart cart) {
        Cart orderCart = cartApplication.refreshCart(cart);
        if (orderCart.getMessages().size() > 0) {
            throw new CustomException(ErrorCode.ORDER_FAIL_CHECK_CART);
        }
        CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();
        Integer orderTotalPrice = getTotalPrice(cart);
        if (customerDto.getBalance() < orderTotalPrice) {
            throw new CustomException(ErrorCode.ORDER_FAIL_NO_MONEY);
        }

        userClient.changeBalance(token, ChangeBalanceForm.builder()
            .from("USER")
            .message("Order")
            .money(-orderTotalPrice)
            .build());

        StringBuilder sb = new StringBuilder("주문 내역");
        for (Cart.Product cartProduct : orderCart.getProducts()) {
            sb.append(cartProduct.getName()).append("의 ");
            for (Cart.ProductItem cartItem : cartProduct.getItems()) {
                ProductItem productItem = productItemService.getProductItem(cartItem.getId());
                productItem.changeCount(productItem.getCount() - cartItem.getCount());
                sb.append("옵션 : ").append(cartItem.getName()).append(" ")
                    .append(cartItem.getCount()).append("개, ");
            }
        }
        sb.append("총 ").append(orderTotalPrice).append("원이 결제되었습니다.");
        cartApplication.clearCart(customerDto.getId());
        mailgunClient.sendEmail(SendMailForm.builder()
            .from("malslapq@gmail.com")
            .to(customerDto.getEmail())
            .subject("주문 내역을 알려드립니다.")
            .text(sb.toString())
            .build());
    }


    public Integer getTotalPrice(Cart cart) {
        return cart.getProducts().stream().flatMapToInt(
                product -> product.getItems().stream().flatMapToInt(
                    productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
            .sum();
    }

}
