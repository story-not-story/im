package com.example.im.entity;

import com.example.im.validate.TextRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

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
    @ApiModelProperty(name = "id", value = "id", dataType = "String", hidden = true)
    @Id
    private String id;
    @ApiModelProperty(name = "userId", value = "用户ID", dataType = "String", example = "1586969516508397974", required = true)
    @TextRange(min = 19, max = 19)
    private String userId;
    @ApiModelProperty(name = "groupId", value = "群聊ID", dataType = "String", example = "1586487898500340241", required = true)
    @TextRange(min = 19, max = 19)
    private String groupId;
    @ApiModelProperty(name = "message", value = "验证信息", dataType = "String", example = "我是南邮学生")
    @TextRange(canNull = true, max = 50)
    private String message;
    @ApiModelProperty(name = "isAccepted", value = "是否同意该加群申请", dataType = "Boolean", hidden = true)
    private Boolean isAccepted;
}
