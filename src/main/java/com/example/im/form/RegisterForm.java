package com.example.im.form;

import com.example.im.validate.Password;
import com.example.im.validate.TextRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HuJun
 * @date 2020/4/9 10:16 下午
 */
@ApiModel(value = "注册对象")
@Data
public class RegisterForm {
    @ApiModelProperty(name = "name", value = "昵称", dataType = "String", required = true, example = "芒果真好吃")
    @TextRange
    private String name;
    @ApiModelProperty(name = "password", value = "密码", dataType = "String", required = true, example = "jsjfl424f")
    @Password
    private String password;
}
