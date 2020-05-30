package com.example.im.form;

import com.example.im.validate.Password;
import com.example.im.validate.TextRange;
import lombok.Data;

/**
 * @author HuJun
 * @date 2020/5/22 6:37 下午
 */
@Data
public class PasswordForm {
    @TextRange(min = 19, max = 19)
    private String id;
    @Password
    private String oldPassword;
    @Password
    private String newPassword;
}
