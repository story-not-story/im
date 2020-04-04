package com.example.im.aspect;

import com.example.im.constant.RedisConstant;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.UserException;
import com.example.im.util.CookieUtil;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;

@Component
@Aspect
@Slf4j
public class UserAuthAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Pointcut("execution(public * com.example.im.controller.*.*(..))" + "&& !execution(public * com.example.im.controller.UserController.*(..))"
            + "&& !execution(public * com.example.im.controller.WebSocket.*(..))")
    public void verify(){}

    @Before("verify()")
    public void doVerify(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Cookie cookie = CookieUtil.get(attributes.getRequest(), CookieUtil.TOKEN);
        if (cookie == null){
            log.error("【用户cookie校验】cookie没有token");
            throw new UserException(ErrorCode.USER_NOT_LOGIN);
        }
        String redisValue = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN, cookie.getValue()));
        if (StringUtils.isNullOrEmpty(redisValue)){
            log.error("【用户cookie校验】redis没有token");
            throw new UserException(ErrorCode.USER_NOT_LOGIN);
        }

    }
}
