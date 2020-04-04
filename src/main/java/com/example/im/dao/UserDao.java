package com.example.im.dao;

import com.example.im.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author HuJun
 * @date 2020/3/21 7:43 下午
 */
public interface UserDao extends JpaRepository<User, String> {
}
