package com.example.im.enums;

import lombok.Getter;

/**
 * @author HuJun
 * @date 2020/4/3 4:26 下午
 */
@Getter
public enum MessageStatus implements CodeEnum{
    /**
     * 消息状态正常
     */
    NORMAL(0, "正常"),
    /**
     * 消息已删除
     */
    DELETED(1, "已删除"),
    /**
     * 消息已撤回
     */
    CANCELED(2, "已撤回");
    private Byte code;
    private String msg;

    MessageStatus(int code, String msg) {
        this.code = (byte) code;
        this.msg = msg;
    }
}
