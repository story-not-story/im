package com.example.im.service.impl;

import com.example.im.dao.LabelDao;
import com.example.im.entity.Label;
import com.example.im.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 12:56 下午
 */
@Service
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelDao labelDao;
    @Override
    public Label save(Label label) {
        return labelDao.save(label);
    }

    @Override
    public void deleteById(Integer id) {
        labelDao.deleteById(id);
    }

    @Override
    public Label findById(Integer id) {
        return labelDao.findById(id).orElse(null);
    }

    @Override
    public List<Label> findByUserId(String userId) {
        return labelDao.findByUserId(userId);
    }
}
