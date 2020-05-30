package com.example.im.validate;

import com.example.im.util.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author HuJun
 * @date 2020/5/12 9:27 下午
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private String regexp;
    private boolean canNull;
    @Override
    public void initialize(Phone constraintAnnotation) {
        canNull = constraintAnnotation.canNull();
        regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (canNull && s == null) {
            return true;
        }
        if (StringUtil.isNullOrEmpty(s)) {
            return false;
        }
        return s.matches(regexp);
    }
}
