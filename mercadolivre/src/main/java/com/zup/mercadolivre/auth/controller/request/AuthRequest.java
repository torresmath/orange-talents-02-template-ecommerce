package com.zup.mercadolivre.auth.controller.request;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthRequest {

    private String login;
    private String password;

    public AuthRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UsernamePasswordAuthenticationToken convert() {
        return new UsernamePasswordAuthenticationToken(login, password);
    }
}
