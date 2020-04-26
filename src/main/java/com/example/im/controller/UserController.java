package com.example.im.controller;

import com.example.im.constant.RedisConstant;
import com.example.im.entity.User;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.UserException;
import com.example.im.form.LoginForm;
import com.example.im.form.RegisterForm;
import com.example.im.result.Result;
import com.example.im.service.LoginService;
import com.example.im.service.UserService;
import com.example.im.util.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author HuJun
 * @date 2020/3/21 8:08 下午
 */
@RestController
@RequestMapping("/")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private LoginService loginService;
    @PostMapping("/register")
    public Result register(@Valid RegisterForm registerForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【用户注册】参数错误");
            throw new UserException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        User user = new User();
        user.setName(registerForm.getName());
        user.setPassword(registerForm.getPassword());
        user.setId(KeyUtil.getUniqueKey());
        User result = userService.save(user);
        Assert.assertNotNull(result);
        Map<String, String> map = new HashMap<>(1);
        map.put("userId", result.getId());
        return ResultUtil.success(map);
    }

    @PostMapping("/login")
    public Result login(@Valid LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response){
        if (bindingResult.hasErrors()){
            log.error("【用户登录】参数错误");
            throw new UserException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        Cookie cookie = CookieUtil.get(request, CookieUtil.TOKEN);
        if (cookie != null){
            log.error("【用户登录】用户已登录");
            throw new UserException(ErrorCode.USER_ALREADY_LOGIN);
        }
        String id = loginForm.getId();
        User user = userService.findById(id);
        if (!user.getPassword().equals(loginForm.getPassword())){
            log.error("【用户登录】密码或用户名错误");
        }
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().setIfAbsent(String.format(RedisConstant.TOKEN, token), id);
        CookieUtil.set(response, CookieUtil.TOKEN, token, CookieUtil.EXPIRE);
        loginService.save(id, true, request);
        return ResultUtil.success();
    }

    @GetMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = CookieUtil.get(request, CookieUtil.TOKEN);
        if (cookie == null){
            log.error("【用户登出】用户已登出");
            throw new UserException(ErrorCode.USER_NOT_LOGIN);
        }
        String token = cookie.getValue();
        CookieUtil.set(response, CookieUtil.TOKEN, null, 0);
        String id = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN, cookie.getValue()));
        redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN, token));
        loginService.save(id, false, request);
        return ResultUtil.success();
    }

    @GetMapping("/userinfo")
    public Result userinfo(@RequestParam String userId){
        User user = userService.findById(userId);
        user.setPassword(null);
        return ResultUtil.success(user);
    }

    @GetMapping("/list")
    public Result list(@RequestParam String text){
        if (Pattern.matches("\\d+", text)) {
            try {
                User user = userService.findById(text);
                user.setPassword(null);
                return ResultUtil.success(user);
            } catch (UserException e) {}
        }
        List<User> userList = userService.findByName(text);
        if (CollectionUtils.isEmpty(userList)) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXISTS);
        }
        userList.forEach(o -> o.setPassword(null));
        return ResultUtil.success(userList);
    }

    @PutMapping("/userinfo")
    public Result userinfoUpdate(@Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【用户注册】参数错误");
            throw new UserException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        User result = userService.findById(user.getId());
        BeanUtil.copyProperties(user, result);
        userService.save(result);
        return ResultUtil.success();
    }
}
