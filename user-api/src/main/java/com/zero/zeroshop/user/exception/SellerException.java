package com.zero.zeroshop.user.exception;

import lombok.Getter;

@Getter
public class SellerException extends RuntimeException{

    private final ErrorCode errorCode;

    public SellerException(ErrorCode errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }

}
