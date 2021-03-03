package com.zup.mercadolivre.purchase.controller;

import com.zup.mercadolivre.purchase.controller.request.PagseguroTransactionRequest;
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
@RequestMapping("/api/v1/pagseguro")
public class PagseguroPurchaseController {

    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private PurchaseCheckout purchaseCheckout;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid PagseguroTransactionRequest request) {
        TransactionCheckin<PagseguroTransactionRequest> transactionCheckin = new TransactionCheckin<>(repository, purchaseCheckout);
        return transactionCheckin.checkIn(request);
    }
}
