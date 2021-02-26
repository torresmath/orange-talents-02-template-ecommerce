package com.zup.mercadolivre.product.controller;

import com.zup.mercadolivre.product.controller.request.ProductRequest;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductDetail;
import com.zup.mercadolivre.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    @PersistenceContext
    EntityManager manager;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@AuthenticationPrincipal UserDetails loggedUser,
                                    @RequestBody @Valid ProductRequest productRequest) {

        User owner = (User) loggedUser;
        productRequest.setOwner(owner);

        Product product = productRequest.toModel(manager);
        manager.persist(product);

        productRequest.getDetails()
                .forEach(detailRequest -> {
                    ProductDetail detail = detailRequest.toModel(product);
                    manager.persist(detail);
                });

        return ResponseEntity.ok().build();
    }
}
