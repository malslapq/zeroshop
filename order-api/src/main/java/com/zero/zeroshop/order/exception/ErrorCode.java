package com.zero.zeroshop.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "중복된 아이템 이름이 존재합니다."),
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    CART_CHANGE_FAIL(HttpStatus.BAD_REQUEST, "장바구니 담기에 실패했습니다."),
    ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품의 수량이 부족합니다."),
    NOT_FOUND_ITEM(HttpStatus.BAD_REQUEST, "아이템을 찾을 수 없습니다."),
    ORDER_FAIL_NO_MONEY(HttpStatus.BAD_REQUEST, "주문 불가! 잔액이 부족합니다."),
    ORDER_FAIL_CHECK_CART(HttpStatus.BAD_REQUEST, "주문 불가! 장바구니를 확인해 주시길 바랍니다."),
    ;


    private final HttpStatus httpStatus;
    private final String detail;


}
