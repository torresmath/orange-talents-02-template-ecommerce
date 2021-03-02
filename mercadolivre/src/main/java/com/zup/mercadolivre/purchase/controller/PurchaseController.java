package com.zup.mercadolivre.purchase.controller;

import com.zup.mercadolivre.product.Mailing;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.purchase.controller.request.PurchaseRequest;
import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PurcharseProductAndAmountValidator purcharseProductAndAmountValidator;

    @Autowired
    private Mailing mailing;

    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(purcharseProductAndAmountValidator);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid PurchaseRequest request,
                                    @AuthenticationPrincipal UserDetails loggedUser) {
        User user = (User) loggedUser;

        Purchase purchase = request.toModel(user, manager);

        manager.persist(purchase);

        String returnUrl = purchase.submitPurchase();
        mailing.send("Cliente quer comprar", purchase.getBuyerEmail(), purchase.getOwnerEmail(), "<html> CONTENT </html>");
        return ResponseEntity.status(302).body(returnUrl);
    }
}

