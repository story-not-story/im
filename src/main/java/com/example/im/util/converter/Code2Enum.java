package com.example.im.util.converter;

import com.example.im.enums.CodeEnum;
import com.example.im.enums.MemberGrade;

/**
 * @author: HuJun
 * @date: 2020/3/21 6:15 下午
 */
public class Code2Enum {
    public static <T extends CodeEnum> T convert(Byte code, Class<T> className){
        for (T each : className.getEnumConstants()) {
            if (each.getCode().equals(code)){
                return each;
            }
        }
        return null;
    }
}
