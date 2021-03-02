package com.zup.mercadolivre.common.validators;

import com.zup.mercadolivre.common.annotations.EnumValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {

    private List<String> validValues;
    private boolean caseSensitive;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        caseSensitive = constraintAnnotation.caseSensitive();
        validValues = Arrays.stream(constraintAnnotation.targetEnum().getEnumConstants())
                .map(Enum::name)
                .map(e -> caseSensitive ? e : e.toUpperCase())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        s = caseSensitive ? s : s.toUpperCase();
        return validValues.contains(s);
    }
}
