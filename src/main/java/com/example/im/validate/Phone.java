package com.example.im.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author HuJun
 * @date 2020/5/13 10:40 下午
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String regexp() default "^1[3456789]\\d{9}$";

    boolean canNull() default false;

    String message() default "password is not formatted";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
