package com.example.im.dao;

import com.example.im.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 7:46 下午
 */
public interface LabelDao extends JpaRepository<Label, Integer> {
    List<Label> findByUserId(String userId);
}
