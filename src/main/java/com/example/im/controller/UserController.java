package com.example.im.controller;

import com.example.im.config.UrlConfig;
import com.example.im.constant.RedisConstant;
import com.example.im.entity.User;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.UserException;
import com.example.im.form.UserForm;
import com.example.im.result.Result;
import com.example.im.service.UserService;
import com.example.im.util.CookieUtil;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author HuJun
 * @date 2020/3/21 8:08 下午
 */
@Controller
@RequestMapping("/")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UrlConfig urlConfig;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @PostMapping("/register")
    @ResponseBody
    public Result register(@Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【用户注册】参数错误");
            throw new UserException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        String id = user.getId();
        if (!StringUtils.isNullOrEmpty(id)){
            log.error("【用户注册】用户已存在");
            throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
        }
        user.setId(KeyUtil.getUniqueKey());
        User result = userService.save(user);
        Assert.assertNotNull(result);
        Map<String, String> map = new HashMap<>();
        map.put("userId", result.getId());
        return ResultUtil.success(map);
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(@Valid UserForm userForm, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response){
        if (bindingResult.hasErrors()){
            log.error("【用户注册】参数错误");
            throw new UserException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        Cookie cookie = CookieUtil.get(request, CookieUtil.TOKEN);
        if (cookie != null){
            log.error("【用户登录】用户已登录");
            throw new UserException(ErrorCode.USER_ALREADY_LOGIN);
        }
        String id = userForm.getId();
        User user = userService.findById(id);
        if (user == null){
            log.error("【用户登录】用户未注册");
            throw new UserException(ErrorCode.USER_NOT_EXISTS);
        }
        if (!user.getPassword().equals(userForm.getPassword())){
            log.error("【用户登录】密码或用户名错误");
        }
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().setIfAbsent(String.format(RedisConstant.TOKEN, token), id);
        CookieUtil.set(response, CookieUtil.TOKEN, token, CookieUtil.EXPIRE);
        return ResultUtil.success();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = CookieUtil.get(request, CookieUtil.TOKEN);
        if (cookie == null){
            log.error("【用户登出】用户已登出");
            throw new UserException(ErrorCode.USER_NOT_LOGIN);
        }
        String token = cookie.getValue();
        CookieUtil.set(response, CookieUtil.TOKEN, null, 0);
        redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN, token));
        return "redirect:" + urlConfig.getHost() + "/login";
    }

    @GetMapping("/userinfo")
    @ResponseBody
    public Result userinfo(@RequestParam String userId){
        User user = userService.findById(userId);
        user.setPassword(null);
        return ResultUtil.success(user);
    }
}
