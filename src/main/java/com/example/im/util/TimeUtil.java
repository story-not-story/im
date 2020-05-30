package com.example.im.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author HuJun
 * @date 2020/5/20 7:39 下午
 */
public class TimeUtil {
    public static Date toDate(String str) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(str);
    }
}
