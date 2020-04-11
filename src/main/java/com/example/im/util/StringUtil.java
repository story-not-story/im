package com.example.im.util;

/**
 * @author HuJun
 * @date 2020/4/10 1:05 上午
 */
public class StringUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty() || str.trim().isEmpty();
    }
}
