package com.example.im.service.impl;

import com.example.im.dao.LabelDao;
import com.example.im.entity.Label;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.LabelException;
import com.example.im.service.LabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author HuJun
 * @date 2020/3/23 12:56 下午
 */
@Service
@Slf4j
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
        Optional<Label> label = labelDao.findById(id);
        if (label.isPresent()) {
            return label.get();
        } else {
            log.error("【根据id查找分组】分组不存在");
            throw new LabelException(ErrorCode.LABEL_NOT_EXISTS);
        }
    }

    @Override
    public List<Label> findByUserId(String userId) {
        return labelDao.findByUserId(userId);
    }
}
