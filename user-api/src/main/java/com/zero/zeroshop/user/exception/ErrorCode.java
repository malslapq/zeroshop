package com.zero.zeroshop.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    WRONG_VERIFICATION(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다."),
    EXPIRE_CODE(HttpStatus.BAD_REQUEST, "인증시간이 만료 됐습니다."),
    NOT_FOUND_USER_FROM_EMAIL(HttpStatus.BAD_REQUEST, "입력한 이메일로 아이디를 찾을 수 없습니다."),
    ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증이 완료되었습니다.");


    private final HttpStatus httpStatus;
    private final String detail;

}
