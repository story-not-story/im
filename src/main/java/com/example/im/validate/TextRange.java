package com.example.im.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author HuJun
 * @date 2020/5/12 9:36 下午
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TextRangeValidator.class)
public @interface TextRange {
    int min() default 1;
    int max() default 18;
    boolean canNull() default false;
    String message() default "text is out of range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
