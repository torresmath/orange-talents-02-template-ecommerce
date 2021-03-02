package com.zup.mercadolivre.common.annotations;

import com.zup.mercadolivre.common.validators.EnumValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Constraint(validatedBy = EnumValueValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {

    String message() default "_invalid_enum";
    Class<? extends Enum<?>> targetEnum();

    boolean caseSensitive() default false;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
