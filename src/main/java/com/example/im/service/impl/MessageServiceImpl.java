package com.example.im.service.impl;

import com.example.im.dao.MessageDao;
import com.example.im.entity.Member;
import com.example.im.entity.Message;
import com.example.im.enums.ErrorCode;
import com.example.im.enums.MessageStatus;
import com.example.im.exception.MessageException;
import com.example.im.service.FriendService;
import com.example.im.service.MemberService;
import com.example.im.service.MessageService;
import com.example.im.util.JsonUtil;
import com.example.im.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private FriendService friendService;

    @Autowired
    private MemberService memberService;
    @Override
    public List<Message> findTop(String userId) {
        List<String> groupIdList = memberService.findByUserId(userId).stream().map(Member::getGroupId).collect(Collectors.toList());
        return messageDao.findTop(userId, groupIdList).stream().filter(o -> o.getIsGroup() || friendService.isFriend(o.getSenderId(), o.getReceiverId())).collect(Collectors.toList());
    }

    @Override
    public List<Message> findByGroupId(String userId, String groupId, Pageable pageable) {
        return messageDao.findByGroupId(groupId, pageable).getContent().stream().filter(o ->
                MessageStatus.NORMAL.getCode().equals(o.getStatus()) || MessageStatus.DELETED.getCode().equals(o.getStatus()) && !JsonUtil.toList(o.getInvisible(), String.class).contains(userId)
        ).collect(Collectors.toList());
    }

    @Override
    public List<Message> findByFriend(String userId, String friendId, Pageable pageable) {
        return messageDao.findByFriend(userId, friendId, pageable).getContent().stream().filter(o ->
                MessageStatus.NORMAL.getCode().equals(o.getStatus()) || MessageStatus.DELETED.getCode().equals(o.getStatus()) && !JsonUtil.toList(o.getInvisible(), String.class).contains(userId)
        ).collect(Collectors.toList());
    }

    @Override
    public void deleteByGroupId(String userId, String groupId) {
        messageDao.findByGroupId(groupId).stream().filter(o ->
                MessageStatus.NORMAL.getCode().equals(o.getStatus()) || MessageStatus.DELETED.getCode().equals(o.getStatus()) && !JsonUtil.toList(o.getInvisible(), String.class).contains(userId)
        ).forEach(o -> delete(o.getId(), userId));
    }

    @Override
    public void deleteByFriend(String userId, String friendId) {
        messageDao.findByFriend(userId, friendId).stream().filter(o ->
                MessageStatus.NORMAL.getCode().equals(o.getStatus()) || MessageStatus.DELETED.getCode().equals(o.getStatus()) && !JsonUtil.toList(o.getInvisible(), String.class).contains(userId)
        ).forEach(o -> delete(o.getId(), userId));
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
            log.error("【删除消息】该消息记录不存在");
            throw new MessageException(ErrorCode.MESSAGE_NOT_EXISTS);
        }
        String invisible = null;
        if (StringUtil.isNullOrEmpty(message.getInvisible())) {
            List<String> userIdList = new ArrayList<>();
            userIdList.add(userId);
            invisible = JsonUtil.toJson(userIdList);
        } else {
            List<String> userIdList = JsonUtil.toList(message.getInvisible(), String.class);
            userIdList.add(userId);
            invisible = JsonUtil.toJson(userIdList);
        }
        message.setInvisible(invisible);
        message.setStatus(MessageStatus.DELETED.getCode());
        messageDao.save(message);
        return message;
    }

    @Override
    public List<Message> findByContentLike(String userId, String content) {
        List<String> groupIdList = memberService.findByUserId(userId).stream().map(Member::getGroupId).collect(Collectors.toList());
        return messageDao.findByContentLike(userId, content, groupIdList).stream().filter(o -> {
            boolean isExists = o.getIsGroup() || friendService.isFriend(o.getReceiverId(), o.getSenderId());
            boolean isVisible = MessageStatus.NORMAL.getCode().equals(o.getStatus()) || MessageStatus.DELETED.getCode().equals(o.getStatus()) && !JsonUtil.toList(o.getInvisible(), String.class).contains(userId);
            return isExists && isVisible;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Message> findByGroupContent(String userId, String groupId, String content, Pageable pageable) {
        return messageDao.findByContentLike(groupId, content, pageable).getContent().stream().filter(o ->
                MessageStatus.NORMAL.getCode().equals(o.getStatus()) || MessageStatus.DELETED.getCode().equals(o.getStatus()) && !JsonUtil.toList(o.getInvisible(), String.class).contains(userId)
        ).collect(Collectors.toList());
    }

    @Override
    public List<Message> findByFriendContent(String userId, String friendId, String content, Pageable pageable) {
        return messageDao.findByContentLike(userId, friendId, content, pageable).getContent().stream().filter(o ->
                MessageStatus.NORMAL.getCode().equals(o.getStatus()) || MessageStatus.DELETED.getCode().equals(o.getStatus()) && !JsonUtil.toList(o.getInvisible(), String.class).contains(userId)
        ).collect(Collectors.toList());
    }

}
