package com.example.im.util;


import java.util.Random;

/**
 * @author: HuJun
 * @date: 2020/3/21 6:15 下午
 */
public class KeyUtil {
    public static synchronized String getUniqueKey(){
        Random random = new Random();
        int i = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(i);
    }
}
