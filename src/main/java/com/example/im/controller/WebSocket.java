package com.example.im.controller;

import com.example.im.entity.Friend;
import com.example.im.entity.Member;
import com.example.im.entity.Message;
import com.example.im.enums.ErrorCode;
import com.example.im.enums.MessageStatus;
import com.example.im.exception.MessageException;
import com.example.im.result.FriendResult;
import com.example.im.result.MessageResult;
import com.example.im.service.FriendService;
import com.example.im.service.LoginService;
import com.example.im.service.MemberService;
import com.example.im.service.MessageService;
import com.example.im.util.JsonUtil;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import com.example.im.util.StringUtil;
import com.example.im.util.converter.DO2VO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HuJun
 * @date 2020/3/21 8:42 下午
 */
@Component
@ServerEndpoint("/websocket/{userId}")
@Slf4j
public class WebSocket {
    private Session session;
    private String userId;
    public static final String HEARTBEAT_REQ = "websocket_test";
    public static final String HEARTBEAT_RES = "ok";
    public static final String BLACKLISTED = "blacklisted";
    private static ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();

    private  static LoginService loginService;
    @Autowired
    public void setLoginService(LoginService loginService){
        WebSocket.loginService = loginService;
    }

    private  static MemberService memberService;
    @Autowired
    public void setMemberService(MemberService memberService){
        WebSocket.memberService = memberService;
    }

    private  static FriendService friendService;
    @Autowired
    public void setFriendService(FriendService friendService){
        WebSocket.friendService = friendService;
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

    @OnError
    public void onError(Session session, Throwable error){
        log.info("发生错误");
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message){
        if (HEARTBEAT_REQ.equals(JsonUtil.getValue(message, "content"))) {
            sendPointMessage(JsonUtil.getValue(message, "userId"), HEARTBEAT_RES, null);
            return;
        }
        log.info("收到消息:{}", message);
        Message msg = JsonUtil.toObject(message, Message.class);
        if (msg == null){
            log.error("【收到消息】消息JSON格式错误");
            throw new MessageException(ErrorCode.JSON_ERROR);
        }
        if (StringUtil.isNullOrEmpty(msg.getId())){
            if (!msg.getIsGroup()) {
                Friend friend = friendService.findOne(msg.getReceiverId(), msg.getSenderId());
                FriendResult result = DO2VO.convert(friend, msg.getSenderId());
                if (result.getIsMeBlacklisted()) {
                    try {
                        webSocketMap.get(msg.getSenderId()).session.getBasicRemote().sendText(BLACKLISTED);
                    } catch(IOException e) {
                        e.printStackTrace();
                        log.error("【websocket点对点消息】I/O发生错误");
                    }
                    return;
                }
            }
            msg.setId(KeyUtil.getUniqueKey());
            msg = messageService.save(msg);
        } else {
            if (MessageStatus.CANCELED.getCode().equals(msg.getStatus())){
                messageService.cancel(msg.getId());
            }
        }
        MessageResult messageResult = DO2VO.convert(msg,  "", true);
        String content = JsonUtil.toJson(ResultUtil.success(messageResult));
        if (StringUtil.isNullOrEmpty(content)){
            log.error("【收到消息】消息JSON格式错误");
            throw new MessageException(ErrorCode.JSON_ERROR);
        }
//        if (!msg.getIsGroup() && !loginService.isLogin(msg.getReceiverId()) && MessageStatus.NORMAL.getCode().equals(msg.getStatus())) {
//            try {
//                webSocketMap.get(msg.getSenderId()).session.getBasicRemote().sendText(content);
//            } catch(IOException e) {
//                e.printStackTrace();
//                log.error("【websocket点对点消息】I/O发生错误");
//            }
//            return;
//        }
        if (msg.getIsGroup()){
            sendGroupMessage(msg.getReceiverId(), content);
        } else {
            sendPointMessage(msg.getReceiverId(), content, msg.getSenderId());
        }
    }

     public void sendGroupMessage(String groupId, String message){
         List<Member> memberList = memberService.findByGroupId(groupId);
         for (Member member:
              memberList) {
             try {
                 String userId = member.getUserId();
                 if (webSocketMap.get(userId) != null) {
                     webSocketMap.get(userId).session.getBasicRemote().sendText(message);
                 }
             } catch (IOException e) {
                 e.printStackTrace();
                 log.error("【websocket广播消息】I/O发生错误");
             }
         }
     }

    public void sendPointMessage(String userId, String message, String senderId){
        try {
            if (webSocketMap.get(userId) != null) {
                webSocketMap.get(userId).session.getBasicRemote().sendText(message);
                if (senderId != null) {
                    webSocketMap.get(senderId).session.getBasicRemote().sendText(message);
                }
            } else if (senderId != null){
                webSocketMap.get(senderId).session.getBasicRemote().sendText(JsonUtil.toJson(ResultUtil.error(ErrorCode.WEBSOCKET_ERROR)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【websocket点对点消息】I/O发生错误");
        }
    }
}
