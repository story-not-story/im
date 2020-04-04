package com.example.im.exception;

import com.example.im.enums.ErrorCode;
import lombok.Getter;

/**
 * @author HuJun
 * @date 2020/3/21 8:22 下午
 */
@Getter
public class UserException extends RuntimeException {
    private Integer code;

    public UserException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }

    public UserException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
