package com.example.im.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author HuJun
 * @date 2020/3/21 8:19 下午
 */
@Data
public class LoginForm {
    @NotNull
    private String id;
    @NotNull
    private String password;
}
