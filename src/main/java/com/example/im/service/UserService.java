package com.example.im.service;

import com.example.im.entity.User;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 7:54 下午
 */
public interface UserService {
    /**
     * 查找用户
     * @param id
     * @return
     */
    User findById(String id);

    /**
     * 根据name模糊匹配查找用户
     * @param name
     * @return
     */
    List<User> findByName(String name);

    /**
     * 保存永辉
     * @param user
     * @return
     */
    User save(User user);

    /**
     * 判断用户是否存在
     * @param id
     * @return
     */
    boolean isExists(String id);
}
