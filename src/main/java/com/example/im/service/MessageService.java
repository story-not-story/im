package com.example.im.service;

import com.example.im.entity.Message;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/4/3 1:54 下午
 */
public interface MessageService {
    List<Message> findAll();
    Message findById(String id);
    Message save(Message message);
    Message cancel(String id);
    Message delete(String id);
}
