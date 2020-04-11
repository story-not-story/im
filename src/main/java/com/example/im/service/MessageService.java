package com.example.im.service;

import com.example.im.entity.Message;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/4/3 1:54 下午
 */
public interface MessageService {
    /**
     * 根据id查找消息
     * @param id
     * @return
     */
    Message findById(String id);

    /**
     * 保存消息
     * @param message
     * @return
     */
    Message save(Message message);

    /**
     * 撤回消息
     * @param id
     * @return
     */
    Message cancel(String id);

    /**
     * 删除消息
     * @param id
     * @return
     */
    Message delete(String id);
}
