package com.zup.mercadolivre.purchase.controller;

import com.zup.mercadolivre.purchase.controller.request.ToTransaction;
import com.zup.mercadolivre.purchase.controller.request.ValidTransactionRequest;
import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.purchase.model.Transaction;
import com.zup.mercadolivre.purchase.repository.PurchaseRepository;
import org.springframework.http.ResponseEntity;

import java.util.Map;


public class TransactionCheckin<T extends ToTransaction & ValidTransactionRequest> {

    private final PurchaseRepository repository;
    private final PurchaseCheckout purchaseCheckout;

    public TransactionCheckin(PurchaseRepository repository, PurchaseCheckout purchaseCheckout) {
        this.repository = repository;
        this.purchaseCheckout = purchaseCheckout;
    }

    public ResponseEntity<Map<String, String>> checkIn(T request) {

        request.validatePurchase(repository);

        Transaction transaction = request.toTransaction();
        Purchase purchase = request.getPurchase();
        purchase.addTransaction(transaction);
        repository.save(purchase);
        purchaseCheckout.commit(purchase);

        return ResponseEntity.ok(Map.of("transaction_status", transaction.getStatus()));
    }
}
