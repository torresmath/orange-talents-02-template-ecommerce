package com.zup.mercadolivre.common.annotations;

import com.zup.mercadolivre.common.validators.UniqueValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueValueValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValue {
    String message() default "_duplicated_field";
    String fieldName();
    Class<?> domainClass();
    boolean enableRegex() default false;
    String regex() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
