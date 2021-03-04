package com.zup.mercadolivre.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceApiController {

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Long> request) {

        Long idPurchase = request.get("id_purchase");
        Long idBuyer = request.get("id_buyer");

        System.out.println("-----INVOICES API-----");
        System.out.println("idBuyer = " + idBuyer);
        System.out.println("idPurchase = " + idPurchase);
        System.out.println("-----INVOICES API-----");

        return ResponseEntity.ok().build();
    }
}
