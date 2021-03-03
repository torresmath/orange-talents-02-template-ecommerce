package com.zup.mercadolivre.purchase.controller;

import com.zup.mercadolivre.purchase.controller.response.PurchaseResponse;
import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.purchase.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/purchase")
public class PurchaseViewController {

    @Autowired
    private PurchaseRepository repository;

    @GetMapping("/{identifier}")
    public ResponseEntity<PurchaseResponse> get(@PathVariable String identifier) {

        Optional<Purchase> possiblePurchase = repository.findByIdentifier(identifier);

        if (possiblePurchase.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Purchase purchase = possiblePurchase.get();

        return ResponseEntity.ok(new PurchaseResponse(purchase));
    }
}
