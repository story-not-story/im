package com.example.im.service;

import com.example.im.entity.Group;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 8:05 下午
 */
public interface GroupService {
    Group findById(String id);
    Group save(Group group);
    void deleteById(String id);
}
