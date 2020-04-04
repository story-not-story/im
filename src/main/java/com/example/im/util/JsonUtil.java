package com.example.im.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: HuJun
 * @date: 2020/3/21 6:55 下午
 */
@Slf4j
public class JsonUtil {
    public static <T> T toObject(String str, Class<T> tClass){
        return JSON.parseObject(str, tClass, Feature.SupportNonPublicField);
    }
    public static String toJson(Object object){
        return JSON.toJSONString(object);
    }
}
