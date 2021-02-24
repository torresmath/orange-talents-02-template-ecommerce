package com.zup.mercadolivre.user.controller.request;

import com.zup.mercadolivre.common.annotations.UniqueValue;
import com.zup.mercadolivre.user.model.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRequest {

    @Email
    @NotBlank
    @UniqueValue(domainClass = User.class, fieldName = "login")
    private final String login;

    @Length(min = 6, max = 40)
    @NotBlank
    private final String password;

    public UserRequest(@Email String login, @Length(min = 6) String password) {
        this.login = login;
        this.password = password;
    }

    public User toModel() {

        return new User(login, password);
    }
}
