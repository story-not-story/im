package com.example.im.validate;

import com.example.im.util.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author HuJun
 * @date 2020/5/12 9:37 下午
 */
public class TextRangeValidator implements ConstraintValidator<TextRange, String> {
    private int min;
    private int max;
    private boolean canNull;
    @Override
    public void initialize(TextRange constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        canNull = constraintAnnotation.canNull();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (canNull && s == null) {
            return true;
        }
        if (StringUtil.isNullOrEmpty(s)) {
            return false;
        } else if (s.length() < min || s.length() > max) {
            return false;
        } else {
            return true;
        }
    }
}
