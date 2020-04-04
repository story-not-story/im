package com.example.im.exception;

import com.example.im.enums.ErrorCode;

/**
 * @author HuJun
 * @date 2020/3/21 10:10 下午
 */
public class MessageException extends RuntimeException {
    private Integer code;

    public MessageException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }

    public MessageException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
