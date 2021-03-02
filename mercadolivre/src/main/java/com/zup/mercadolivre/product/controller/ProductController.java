package com.zup.mercadolivre.product.controller;

import com.zup.mercadolivre.product.controller.request.ProductDetailRequest;
import com.zup.mercadolivre.product.controller.request.ProductRequest;
import com.zup.mercadolivre.product.controller.response.ProductViewResponse;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductDetail;
import com.zup.mercadolivre.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    @PersistenceContext
    EntityManager manager;

    @GetMapping("/{id}")
    public ResponseEntity<ProductViewResponse> get(@PathVariable Long id) {

        Product product = manager.find(Product.class, id);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new ProductViewResponse(product));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@AuthenticationPrincipal UserDetails loggedUser,
                                    @RequestBody @Valid ProductRequest productRequest) {

        User owner = (User) loggedUser;
        productRequest.setOwner(owner);

        Product product = productRequest.toModel(manager);

        manager.persist(product);

        return ResponseEntity.ok().build();
    }
}
