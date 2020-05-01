package com.example.im.service.impl;

import com.example.im.dao.MessageDao;
import com.example.im.entity.Member;
import com.example.im.entity.Message;
import com.example.im.enums.ErrorCode;
import com.example.im.enums.MessageStatus;
import com.example.im.exception.MessageException;
import com.example.im.service.MemberService;
import com.example.im.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author HuJun
 * @date 2020/4/3 2:08 下午
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MemberService memberService;
    @Override
    public List<Message> findTop(String userId) {
        List<String> groupIdList = memberService.findByUserId(userId).stream().map(Member::getGroupId).collect(Collectors.toList());
        return messageDao.findTop(userId, groupIdList);
    }

    @Override
    public List<Message> findByGroupId(String groupId, Pageable pageable) {
        return messageDao.findByGroupId(groupId, pageable);
    }

    @Override
    public List<Message> findByFriend(String userId, String friendId, Pageable pageable) {
        return messageDao.findByFriend(userId, friendId, pageable);
    }

    @Override
    public Message findById(String id) {
        Optional<Message> message = messageDao.findById(id);
        if (message.isPresent()) {
            return message.get();
        } else {
            log.error("【根据id查找消息】消息不存在");
            throw new MessageException(ErrorCode.MESSAGE_NOT_EXISTS);
        }
    }

    @Override
    public Message save(Message message) {
        return messageDao.save(message);
    }

    @Override
    public Message cancel(String id) {
        Message message = findById(id);
        message.setStatus(MessageStatus.CANCELED.getCode());
        messageDao.save(message);
        return message;
    }

    @Override
    public Message delete(String id, String userId) {
        Message message = findById(id);
        if (message == null){
            log.error("【撤回消息】该消息记录不存在");
            throw new MessageException(ErrorCode.MESSAGE_NOT_EXISTS);
        }
        if (userId.equals(message.getSenderId())) {
            if (MessageStatus.NORMAL.getCode().equals(message.getStatus())) {
                message.setStatus(MessageStatus.VISIBLE_R.getCode());
            } else if (MessageStatus.VISIBLE_S.getCode().equals(message.getStatus())){
                message.setStatus(MessageStatus.CANCELED.getCode());
            }
        } else {
            if (MessageStatus.NORMAL.getCode().equals(message.getStatus())) {
                message.setStatus(MessageStatus.VISIBLE_S.getCode());
            } else if (MessageStatus.VISIBLE_R.getCode().equals(message.getStatus())){
                message.setStatus(MessageStatus.CANCELED.getCode());
            }
        }
        messageDao.save(message);
        return message;
    }

    @Override
    public List<Message> findByContentLike(String userId, String content) {
        return messageDao.findByContentLike(userId, content);
    }

}
