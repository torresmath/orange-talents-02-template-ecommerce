package com.zup.mercadolivre.common.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationErrorHandler {

    @Autowired
    private MessageSource source;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorOutputDto handle(MethodArgumentNotValidException exception) {
        ValidationErrorOutputDto errorOutputDto = new ValidationErrorOutputDto();

        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    FieldValidationErrorOutputDto fieldError = new FieldValidationErrorOutputDto(
                            error.getField(),
                            source.getMessage(error, LocaleContextHolder.getLocale()));

                    errorOutputDto.addError(fieldError);
                });

        exception.getBindingResult().getGlobalErrors()
                .forEach(error -> errorOutputDto.addGlobalError(
                        source.getMessage(error, LocaleContextHolder.getLocale())
                ));

        return errorOutputDto;
    }

}
