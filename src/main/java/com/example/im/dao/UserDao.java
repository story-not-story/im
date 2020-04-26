package com.example.im.dao;

import com.example.im.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 7:43 下午
 */
public interface UserDao extends JpaRepository<User, String> {
    @Query("select u from User u where u.name like %?1% or u.id like %?1%")
    List<User> findByNameIdLike(String name);
}
