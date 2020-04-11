package com.example.im.service;

import com.example.im.entity.Login;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author HuJun
 * @date 2020/4/11 6:20 下午
 */
public interface LoginService {
    /**
     * 保存登录登出信息
     * @param userId
     * @param status
     * @param request
     * @return
     */
    Login save(String userId, Boolean status, HttpServletRequest request);

    /**
     * 根据userId查找用户登录登出信息
     * @param userId
     * @return
     */
    List<Login> findByUserId(String userId);
}
