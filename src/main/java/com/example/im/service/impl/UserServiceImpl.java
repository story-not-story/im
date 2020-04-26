package com.example.im.service.impl;

import com.example.im.dao.UserDao;
import com.example.im.entity.User;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * @author HuJun
 * @date 2020/3/21 7:56 下午
 */
@Service
@Slf4j
public class UserServiceImpl implements com.example.im.service.UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public User findById(String id) {
        Optional<User> user = userDao.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            log.error("【按照id查找用户】用户不存在");
            throw new UserException(ErrorCode.USER_NOT_EXISTS);
        }
    }

    @Override
    public List<User> findByName(String name) {
        return userDao.findByNameIdLike(name);
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public boolean isExists(String id) {
        if (userDao.findById(id).isPresent()){
            return true;
        }
        return false;
    }
}
