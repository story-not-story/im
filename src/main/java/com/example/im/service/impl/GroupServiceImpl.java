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
        return groupDao.findById(id).orElse(null);
    }

    @Override
    public Group save(Group group) {
        return groupDao.save(group);
    }

    @Override
    public void deleteById(String id) {
        Group group = findById(id);
        if (group == null){
            log.error("【解散群】群不存在");
            throw new GroupException(ErrorCode.GROUP_NOT_EXISTS);
        }
        groupDao.deleteById(id);
    }
}
