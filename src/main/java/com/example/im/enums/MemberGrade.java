package com.example.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HuJun
 * @date 2020/3/23 3:37 下午
 */
@Getter
public enum MemberGrade implements CodeEnum{
    NORMAL(0, "普通"),
    MANAGER(1, "管理员"),
    OWNER(2, "群主");
    private Byte code;
    private String msg;

    MemberGrade(int code, String msg) {
        this.code = (byte)  code;
        this.msg = msg;
    }
}
