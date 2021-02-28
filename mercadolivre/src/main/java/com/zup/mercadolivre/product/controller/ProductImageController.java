package com.zup.mercadolivre.product.controller;

import com.zup.mercadolivre.product.controller.request.ProductImageRequest;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/product/{id}/image")
public class ProductImageController {

    @PersistenceContext
    EntityManager manager;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping
    public ResponseEntity<?> create(@PathVariable("id") Long idProduct,
                                    @AuthenticationPrincipal UserDetails loggedUser,
                                    @RequestBody @Valid ProductImageRequest request) {

        Product product = manager.find(Product.class, idProduct);

        validateUserAndProduct((User) loggedUser, product);

        publisher.publishEvent(request.toEvent(product));

        return ResponseEntity.ok().build();
    }

    void validateUserAndProduct(User loggedUser, Product product) {
        Optional.ofNullable(product).ifPresentOrElse(
                validProduct -> {
                    if (!validProduct.getOwner().equals(loggedUser))
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto não encontrado");
                });
    }
}
