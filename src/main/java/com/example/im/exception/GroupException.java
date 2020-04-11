package com.example.im.exception;

import com.example.im.enums.ErrorCode;
import lombok.Getter;

/**
 * @author HuJun
 * @date 2020/3/23 12:41 下午
 */
@Getter
public class GroupException extends RuntimeException  {
    private Integer code;

    public GroupException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }

    public GroupException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
