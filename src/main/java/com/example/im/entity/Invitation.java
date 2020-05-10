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
 * @date 2020/3/21 7:34 下午
 */
@ApiModel(value = "加好友申请")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class Invitation {
    @ApiModelProperty(name = "id", value = "加好友申请id", dataType = "String")
    @Id
    private String id;
    @ApiModelProperty(name = "senderId", value = "发送者id", dataType = "String", example = "1586969516508397974", required = true)
    @NotNull
    private String senderId;
    @ApiModelProperty(name = "receiverId", value = "接收者id", dataType = "String", example = "1586969360085913084", required = true)
    @NotNull
    private String receiverId;
    @ApiModelProperty(name = "labelId", value = "分组id", dataType = "int", example = "1")
    private Integer labelId;
    @ApiModelProperty(name = "message", value = "验证信息", dataType = "String", example = "我是南邮学生")
    private String message;
    @ApiModelProperty(name = "isAccepted", value = "是否同意该加群申请", dataType = "Boolean")
    private Boolean isAccepted;
    @ApiModelProperty(name = "remark", value = "备注", dataType = "String", example = "玲珑")
    private String remark;
}
