package com.example.im.validate;

import com.example.im.util.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author HuJun
 * @date 2020/5/12 9:27 下午
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {
    private String regexp;
    @Override
    public void initialize(Password constraintAnnotation) {
        regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtil.isNullOrEmpty(s)) {
            return false;
        }
        return s.matches(regexp);
    }
}
