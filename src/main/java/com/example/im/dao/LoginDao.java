package com.example.im.dao;


import com.example.im.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query(value = "select * from login where user_id = :userId order by gmt_create desc limit 1", nativeQuery = true)
    Login findTopByUserId(@Param("userId") String userId);
}
