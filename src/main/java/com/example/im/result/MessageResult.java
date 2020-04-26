package com.example.im.result;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author HuJun
 * @date 2020/4/17 12:42 上午
 */
@Data
public class MessageResult {
    private String id;
    private String senderId;
    private String receiverId;
    private String name;
    private String avatar;
    private Boolean isGroup;
    private Byte status = 0;
    private Byte type;
    private String toUserId;
    private Timestamp finishTime;
    private String content;
    private Timestamp gmtCreate;
}
