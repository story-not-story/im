package com.example.im.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: HuJun
 * @date: 2020/3/21 6:55 下午
 */
public class CookieUtil {
    public static final String TOKEN = "token";
    public static final int EXPIRE = 7200;
    public static void set(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static Cookie get(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        Map<String, Cookie> map = new HashMap<>(cookies.length);
        for (Cookie cookie : cookies) {
            map.put(cookie.getName(), cookie);
        }
        return map.getOrDefault(name, null);
    }
}
