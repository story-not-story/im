package com.example.im.service.impl;

import com.example.im.dao.UserDao;
import com.example.im.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author HuJun
 * @date 2020/3/21 7:56 下午
 */
@Service
public class UserServiceImpl implements com.example.im.service.UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public User findById(String id) {
        return userDao.findById(id).orElse(null);
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
