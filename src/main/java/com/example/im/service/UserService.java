package com.example.im.service;

import com.example.im.entity.User;

/**
 * @author HuJun
 * @date 2020/3/21 7:54 下午
 */
public interface UserService {
    User findById(String id);
    User save(User user);
    boolean isExists(String id);
}
