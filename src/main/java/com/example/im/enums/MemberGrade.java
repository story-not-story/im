package com.example.im.enums;

import lombok.Getter;

/**
 * @author HuJun
 * @date 2020/3/23 3:37 下午
 */
@Getter
public enum MemberGrade implements CodeEnum{
    /**
     * 普通群成员
     */
    NORMAL(0, "普通"),
    /**
     * 群管理员
     */
    MANAGER(1, "管理员"),
    /**
     * 群主
     */
    OWNER(2, "群主");
    private Byte code;
    private String msg;

    MemberGrade(int code, String msg) {
        this.code = (byte)  code;
        this.msg = msg;
    }
}
