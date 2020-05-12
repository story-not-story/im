package com.example.im.form;

import com.example.im.validate.Password;
import com.example.im.validate.TextRange;
import lombok.Data;

/**
 * @author HuJun
 * @date 2020/3/21 8:19 下午
 */
@Data
public class LoginForm {
    @TextRange(min = 19, max = 19)
    private String id;
    @Password
    private String password;
}
