package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author HuJun
 * @date 2020/3/21 7:38 下午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class Message {
    @Id
    private String id;
    private String senderId;
    private String receiverId;
    @NotNull
    private Boolean isGroup;
    private Byte status = 0;
    private Byte type = 0;
    private String toUserId;
    private Timestamp finishTime;
    private String content;
    private String invisible;
    private Timestamp gmtCreate;
}
