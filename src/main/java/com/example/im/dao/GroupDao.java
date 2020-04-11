package com.example.im.dao;

import com.example.im.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author HuJun
 * @date 2020/3/21 7:44 下午
 */
public interface GroupDao extends JpaRepository<Group, String> {
}
