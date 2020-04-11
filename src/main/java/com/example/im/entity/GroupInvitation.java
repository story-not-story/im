package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author HuJun
 * @date 2020/4/10 11:32 上午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class GroupInvitation {
    @Id
    private String id;
    @NotNull
    private String senderId;
    @NotNull
    private String receiverId;
    @NotNull
    private String groupId;
    private Boolean isAccepted;
}
