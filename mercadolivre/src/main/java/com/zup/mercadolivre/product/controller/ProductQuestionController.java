package com.zup.mercadolivre.product.controller;

import com.zup.mercadolivre.product.Mailing;
import com.zup.mercadolivre.product.controller.request.ProductQuestionRequest;
import com.zup.mercadolivre.product.controller.response.ProductQuestionResponse;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductQuestion;
import com.zup.mercadolivre.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/product/{id}/question")
public class ProductQuestionController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private Mailing mailing;

    @PostMapping
    @Transactional
    public ResponseEntity<List<ProductQuestionResponse>> create(@PathVariable("id") Long idProduct,
                                    @RequestBody @Valid ProductQuestionRequest request,
                                    @AuthenticationPrincipal UserDetails loggedUser) {

        User user = (User) loggedUser;

        Product product = manager.find(Product.class, idProduct);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        ProductQuestion productQuestion = request.toModel(product,user);
        manager.persist(productQuestion);

        mailing.send("Dúvida sobre produto", productQuestion.getCustomer(), product.getOwnerEmail(), productQuestion.getTitle());

        List<ProductQuestionResponse> questions = manager.createQuery("select q from ProductQuestion q where q.product = :product", ProductQuestion.class)
                .setParameter("product", product)
                .getResultList()
                .stream()
                .map(ProductQuestionResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(questions);
    }
}
