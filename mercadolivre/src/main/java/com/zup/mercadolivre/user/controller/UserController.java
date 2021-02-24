package com.zup.mercadolivre.user.controller;

import com.zup.mercadolivre.user.model.User;
import com.zup.mercadolivre.user.controller.request.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @PersistenceContext
    EntityManager manager;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid UserRequest userRequest) {

        User user = userRequest.toModel();
        manager.persist(user);

        return ResponseEntity.ok().build();
    }
}
