package com.example.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HuJun
 * @date 2020/4/3 4:26 下午
 */
@Getter
public enum MessageStatus implements CodeEnum{
    NORMAL(0, "正常"),
    DELETED(1, "已删除"),
    CANCELED(2, "已撤回");
    private Byte code;
    private String msg;

    MessageStatus(int code, String msg) {
        this.code = (byte) code;
        this.msg = msg;
    }
}
