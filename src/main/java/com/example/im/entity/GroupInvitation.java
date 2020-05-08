package com.example.im.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "拉好友进群的邀请")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class GroupInvitation {
    @ApiModelProperty(name = "id", value = "拉好友进群的邀请id", dataType = "String", example = "1588590953614706732")
    @Id
    private String id;
    @ApiModelProperty(name = "senderId", value = "发送者id", dataType = "String", example = "1586969516508397974", required = true)
    @NotNull
    private String senderId;
    @ApiModelProperty(name = "receiverId", value = "接收者id", dataType = "String", example = "1586969360085913084", required = true)
    @NotNull
    private String receiverId;
    @ApiModelProperty(name = "groupId", value = "群聊id", dataType = "String", example = "1586487898500340241", required = true)
    @NotNull
    private String groupId;
    @ApiModelProperty(name = "isAccepted", value = "是否同意邀请", dataType = "Boolean", example = "true")
    private Boolean isAccepted;
}
