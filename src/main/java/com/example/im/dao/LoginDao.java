package com.example.im.dao;


import com.example.im.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 7:46 下午
 */
public interface LoginDao extends JpaRepository<Login, String> {
    /**
     * 根据userId查找用户登录登出信息
     * @param userId
     * @return
     */
    List<Login> findByUserId(String userId);
}
