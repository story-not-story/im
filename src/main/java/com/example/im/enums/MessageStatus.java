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
     * 消息已删除，不可见用户列表见invisible列
     */
    DELETED(2, "已删除");
    private Byte code;
    private String msg;

    MessageStatus(int code, String msg) {
        this.code = (byte) code;
        this.msg = msg;
    }
}
