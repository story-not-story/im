package com.example.im.service;

import com.example.im.entity.Label;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 12:56 下午
 */
public interface LabelService {
    /**
     * 保存分组
     * @param label
     * @return
     */
    Label save(Label label);

    /**
     * 删除分组
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 查找分组
     * @param id
     * @return
     */
    Label findById(Integer id);

    /**
     * 根据userId查找分组
     * @param userId
     * @return
     */
    List<Label> findByUserId(String userId);
}
