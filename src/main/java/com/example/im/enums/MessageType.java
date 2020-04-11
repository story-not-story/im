package com.example.im.enums;

import lombok.Getter;

/**
 * @author HuJun
 * @date 2020/4/3 4:30 下午
 */
@Getter
public enum MessageType implements CodeEnum{
    /**
     * 文字消息
     */
    WORD(0, "文字"),
    /**
     * 表情消息
     */
    EMOJI(1, "表情"),
    /**
     *  图片消息
     */
    PICTURE(2, "图片"),
    /**
     * 文件消息
     */
    FILE(3, "文件"),
    /**
     * 视频消息
     */
    VIDEO(4, "视频"),
    /**
     * 视频通话或语音通话
     */
    CALL(5, "通话");
    private Byte code;
    private String msg;

    MessageType(int code, String msg) {
        this.code = (byte) code;
        this.msg = msg;
    }
}
