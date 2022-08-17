package com.zero.zeroshop.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "중복된 아이템 이름이 존재합니다."),
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String detail;


}
