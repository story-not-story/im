package com.example.im.service;

import com.example.im.entity.Label;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 12:56 下午
 */
public interface LabelService {
    Label save(Label label);
    void deleteById(int id);
    Label findById(int id);
    List<Label> findByUserId(String userId);
}
