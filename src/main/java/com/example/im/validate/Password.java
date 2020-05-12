package com.example.im.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author HuJun
 * @date 2020/5/12 9:22 下午
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String regexp() default "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";

    String message() default "password is not formatted";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
