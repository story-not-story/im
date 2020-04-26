package com.example.im.service.impl;

import com.example.im.dao.GroupDao;
import com.example.im.entity.Group;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.GroupException;
import com.example.im.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author HuJun
 * @date 2020/3/21 8:05 下午
 */
@Service
@Slf4j
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    @Override
    public Group findById(String id) {
        Optional<Group> group = groupDao.findById(id);
        if (group.isPresent()) {
            return group.get();
        } else {
            log.error("【按照id查找群聊】群聊不存在");
            throw new GroupException(ErrorCode.GROUP_NOT_EXISTS);
        }
    }

    @Override
    public List<Group> findByName(String name) {
        return groupDao.findByNameIdLike(name);
    }

    @Override
    public Group save(Group group) {
        return groupDao.save(group);
    }

    @Override
    public void deleteById(String id) {
        Group group = findById(id);
        groupDao.deleteById(id);
    }
}
