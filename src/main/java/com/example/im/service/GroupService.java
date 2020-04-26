package com.example.im.service;

import com.example.im.entity.Group;

import java.util.List;


/**
 * @author HuJun
 * @date 2020/3/21 8:05 下午
 */
public interface GroupService {
    /**
     * 根据id查找群聊
     * @param id
     * @return
     */
    Group findById(String id);

    /**
     * 根据name模糊匹配查找群聊
     * @param name
     * @return
     */
    List<Group> findByName(String name);

    /**
     * 保存群
     * @param group
     * @return
     */
    Group save(Group group);

    /**
     * 根据id删除群聊
     * @param id
     */
    void deleteById(String id);
}
