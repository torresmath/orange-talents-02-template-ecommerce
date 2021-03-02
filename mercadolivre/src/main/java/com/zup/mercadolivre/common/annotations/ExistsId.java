package com.zup.mercadolivre.common.annotations;

import com.zup.mercadolivre.common.validators.ExistsIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistsIdValidator.class})
public @interface ExistsId {

    String message() default "_not_found";
    Class<?> domainClass();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
