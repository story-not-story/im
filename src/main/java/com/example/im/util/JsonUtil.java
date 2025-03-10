package com.example.im.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: HuJun
 * @date: 2020/3/21 6:55 下午
 */
@Slf4j
public class JsonUtil {
    public static <T> T toObject(String str, Class<T> tClass){
        return JSON.parseObject(str, tClass, Feature.SupportNonPublicField);
    }
    public static <T> List<T> toList(String str, Class<T> tClass) {
        return JSON.parseArray(str, tClass);
    }
    public static String toJson(Object object){
        return JSON.toJSONString(object);
    }
    public static String getValue(String str, String key) {
        return JSON.parseObject(str).getString(key);
    }
}
