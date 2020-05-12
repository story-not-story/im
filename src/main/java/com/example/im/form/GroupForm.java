package com.example.im.form;

import com.example.im.validate.TextRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/5/12 9:11 下午
 */
@ApiModel
@Data
public class GroupForm {
    @ApiModelProperty(name = "userId", value = "用户ID", dataType = "String", required = true, example = "1586969360085913084")
    @TextRange(min = 19, max = 19)
    private String userId;
    @ApiModelProperty(name = "name", value = "群聊名称", dataType = "String", example = "听风文学社")
    @TextRange(canNull = true)
    private String name;
    @ApiModelProperty(name = "signature", value = "群签名", dataType = "String", example = "文字感动心灵")
    private String signature;
    @ApiModelProperty(name = "userIdList", value = "群成员ID列表", dataType = "List", example = "[1586969516508397974, 1587483395687238129]")
    private List<String> userIdList;
}
