package com.example.im.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author HuJun
 * @date 2020/4/9 10:16 下午
 */
@Data
public class RegisterForm {
    @NotNull
    private String name;
    @NotNull
    private String password;
}
