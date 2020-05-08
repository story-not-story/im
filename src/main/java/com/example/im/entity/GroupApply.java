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
 * @date 2020/4/11 8:00 下午
 */
@ApiModel(value = "加群申请")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class GroupApply {
    @ApiModelProperty(name = "id", value = "id", dataType = "String", example = "1588590953614706732")
    @Id
    private String id;
    @ApiModelProperty(name = "userId", value = "用户ID", dataType = "String", example = "1586969516508397974", required = true)
    @NotNull
    private String userId;
    @ApiModelProperty(name = "groupId", value = "群聊ID", dataType = "String", example = "1586487898500340241", required = true)
    @NotNull
    private String groupId;
    @ApiModelProperty(name = "message", value = "验证信息", dataType = "String", example = "我是南邮学生")
    private String message;
    @ApiModelProperty(name = "isAccepted", value = "是否同意该加群申请", dataType = "Boolean", example = "true")
    private Boolean isAccepted;
}
