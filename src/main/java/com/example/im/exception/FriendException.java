package com.example.im.exception;

import com.example.im.enums.ErrorCode;
import lombok.Getter;

/**
 * @author HuJun
 * @date 2020/3/21 10:10 下午
 */
@Getter
public class FriendException extends RuntimeException {
    private Integer code;

    public FriendException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }

    public FriendException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
