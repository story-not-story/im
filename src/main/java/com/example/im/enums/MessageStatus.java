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
     * 消息对senderId、receiverId都不可见
     */
    CANCELED(1, "已撤回"),
    /**
     * 消息只对senderId可见
     */
    VISIBLE_S(2, "消息只对senderId可见"),
    /**
     * 消息只对receiverId可见
     */
    VISIBLE_R(3, "消息只对receiverId可见");
    private Byte code;
    private String msg;

    MessageStatus(int code, String msg) {
        this.code = (byte) code;
        this.msg = msg;
    }
}
