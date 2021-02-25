package com.zup.mercadolivre.auth.controller;

import com.zup.mercadolivre.auth.controller.request.AuthRequest;
import com.zup.mercadolivre.common.configurations.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<String> auth(@RequestBody @Valid AuthRequest authRequest) {

        System.out.println("Auth controller");
        try {
            Authentication authentication = authManager.authenticate(authRequest.convert());
            String token = tokenService.generateToken(authentication);

            return ResponseEntity.ok(token);
        } catch (ArithmeticException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
