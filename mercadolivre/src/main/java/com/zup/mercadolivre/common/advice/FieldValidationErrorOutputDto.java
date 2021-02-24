package com.zup.mercadolivre.common.advice;

public class FieldValidationErrorOutputDto {

    private String field;
    private String error;

    public FieldValidationErrorOutputDto(String message, String error) {
        this.field = message;
        this.error = error;
    }

    public String getField() {
        return field;
    }

    public String getError() {
        return error;
    }
}

