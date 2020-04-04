package com.example.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HuJun
 * @date 2020/4/3 4:30 下午
 */
@Getter
public enum MessageType implements CodeEnum{
    WORD(0, "文字"),
    EMOJI(1, "表情"),
    PICTURE(2, "图片"),
    FILE(3, "文件"),
    VIDEO(4, "视频"),
    CALL(5, "通话");
    private Byte code;
    private String msg;

    MessageType(int code, String msg) {
        this.code = (byte) code;
        this.msg = msg;
    }
}
