package com.zup.mercadolivre.product.controller;

import com.zup.mercadolivre.product.controller.request.ProductOpinionRequest;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductOpinion;
import com.zup.mercadolivre.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/product/{id}/opinion")
public class ProductOpinionController {

    @PersistenceContext
    private EntityManager manager;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@PathVariable("id") Long idProduct,
                                    @RequestBody @Valid ProductOpinionRequest opinionRequest,
                                    @AuthenticationPrincipal UserDetails loggedUser) {
        User customer = (User) loggedUser;


        Product product = manager.find(Product.class, idProduct);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        ProductOpinion productOpinion = opinionRequest.toModel(product, customer);
        manager.persist(productOpinion);

        return ResponseEntity.ok().build();
    }
}
