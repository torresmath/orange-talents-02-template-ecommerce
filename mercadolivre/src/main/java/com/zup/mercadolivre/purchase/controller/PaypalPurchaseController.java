package com.zup.mercadolivre.purchase.controller;

import com.zup.mercadolivre.purchase.controller.request.PaypalTransactionRequest;
import com.zup.mercadolivre.purchase.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/paypal")
public class PaypalPurchaseController {

    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private PurchaseCheckout purchaseCheckout;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid PaypalTransactionRequest request) {
        TransactionCheckin<PaypalTransactionRequest> transactionCheckin = new TransactionCheckin<>(repository, purchaseCheckout);
        return transactionCheckin.checkIn(request);
    }
}
