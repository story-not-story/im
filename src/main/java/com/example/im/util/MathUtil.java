package com.example.im.util;

/**
 * @author: HuJun
 * @date: 2020/3/21 6:55 下午
 */
public class MathUtil {
    private static final double DISTANCE = 0.0001;
    public static boolean isEqual(Double d1, Double d2){
        if (Math.abs(d1 - d2) < DISTANCE){
            return true;
        }
        return false;
    }
}
