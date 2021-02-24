package com.zup.mercadolivre.validations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorOutputDto {

    @JsonProperty("field_errors")
    private final List<FieldValidationErrorOutputDto> fieldErrors = new ArrayList<>();
    @JsonProperty("global_errors")
    private final List<String> globalErrors = new ArrayList<>();

    public void addError(FieldValidationErrorOutputDto fieldError) {
        fieldErrors.add(fieldError);

    }

    public void addGlobalError(String globalError) {
        globalErrors.add(globalError);
    }

    public List<FieldValidationErrorOutputDto> getFieldErrors() {
        return fieldErrors;
    }
}
