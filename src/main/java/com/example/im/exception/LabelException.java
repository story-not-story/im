package com.example.im.exception;

import com.example.im.enums.ErrorCode;

/**
 * @author HuJun
 * @date 2020/3/28 10:06 上午
 */
public class LabelException extends RuntimeException  {
    private Integer code;

    public LabelException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }

    public LabelException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
