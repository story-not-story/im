package com.example.im.controller;

import com.example.im.DTO.MessageDTO;
import com.example.im.entity.Member;
import com.example.im.entity.Message;
import com.example.im.enums.ErrorCode;
import com.example.im.enums.MessageStatus;
import com.example.im.exception.MessageException;
import com.example.im.service.MemberService;
import com.example.im.service.MessageService;
import com.example.im.util.JsonUtil;
import com.example.im.util.KeyUtil;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{userId}")
@Slf4j
public class WebSocket {
    private Session session;
    private String userId;
    private static ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();


    private  static MemberService memberService;
    @Autowired
    public void setMemberService(MemberService memberService){
        WebSocket.memberService = memberService;
    }

    private static MessageService messageService;
    @Autowired
    public void setMessageService(MessageService messageService){
        WebSocket.messageService = messageService;
    }
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        webSocketMap.put(userId, this);
        log.info("一个新的链接，现在连接数：{}", webSocketMap.size());
    }
    @OnClose
    public void onClose(){
        webSocketMap.remove(this.userId);
        log.info("断开一个链接，现在连接数：{}", webSocketMap.size());
    }
    @OnMessage
    public void onMessage(String message){
        log.info("收到消息:{}", message);
        Message msg = JsonUtil.toObject(message, Message.class);
        if (msg == null){
            log.error("【收到消息】消息JSON格式错误");
            throw new MessageException(ErrorCode.JSON_ERROR);
        }
        if (StringUtils.isNullOrEmpty(msg.getId())){
            msg.setId(KeyUtil.getUniqueKey());
            messageService.save(msg);
        } else {
            if (MessageStatus.CANCELED.getCode().equals(msg.getStatus())){
                messageService.cancel(msg.getId());
            }
            if (MessageStatus.DELETED.getCode().equals(msg.getStatus())){
                messageService.delete(msg.getId());
            }
        }
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(msg.getId());
        messageDTO.setContent(msg.getContent());
        messageDTO.setStatus(msg.getStatus());
        String content = JsonUtil.toJson(messageDTO);
        if (StringUtils.isNullOrEmpty(content)){
            log.error("【收到消息】消息JSON格式错误");
            throw new MessageException(ErrorCode.JSON_ERROR);
        }
        if (msg.getIsGroup()){
            sendGroupMessage(msg.getReceiverId(), content);
        } else {
            sendPointMessage(msg.getReceiverId(), content);
        }
    }
    @OnError
    public void onError(Session session, Throwable error){
        log.info("发生错误");
        error.printStackTrace();
    }
     public void sendGroupMessage(String groupId, String message){
         List<Member> memberList = memberService.findByGroupId(groupId);
         for (Member member:
              memberList) {
             try {
                 webSocketMap.get(member.getUserId()).session.getBasicRemote().sendText(message);

             } catch (IOException e) {
                 e.printStackTrace();
                 log.error("【websocket广播消息】I/O发生错误");
             }
         }
     }

    public void sendPointMessage(String userId, String message){
        try {
            webSocketMap.get(userId).session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【websocket点对点消息】I/O发生错误");
        }
    }
}
