package com.example.im.service.impl;

import com.example.im.dao.MessageDao;
import com.example.im.entity.Message;
import com.example.im.enums.ErrorCode;
import com.example.im.enums.MessageStatus;
import com.example.im.exception.MessageException;
import com.example.im.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/4/3 2:08 下午
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;
    @Override
    public List<Message> findAll() {
        return messageDao.find();
    }

    @Override
    public Message findById(String id) {
        return messageDao.findById(id).orElse(null);
    }

    @Override
    public Message save(Message message) {
        return messageDao.save(message);
    }

    @Override
    public Message cancel(String id) {
        Message message = findById(id);
        if (message == null){
            log.error("【撤回消息】该消息记录不存在");
            throw new MessageException(ErrorCode.MESSAGE_NOT_EXISTS);
        }
        message.setStatus(MessageStatus.CANCELED.getCode());
        messageDao.save(message);
        return message;
    }

    @Override
    public Message delete(String id) {
        Message message = findById(id);
        if (message == null){
            log.error("【撤回消息】该消息记录不存在");
            throw new MessageException(ErrorCode.MESSAGE_NOT_EXISTS);
        }
        message.setStatus(MessageStatus.DELETED.getCode());
        messageDao.save(message);
        return message;
    }

}
