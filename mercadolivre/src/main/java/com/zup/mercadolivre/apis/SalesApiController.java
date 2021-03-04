package com.zup.mercadolivre.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sales")
public class SalesApiController {

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Map<String, Long> request) {

        System.out.println("-----SALES API-----");

        Long idPurchase = request.get("id_purchase");
        Long idOwner = request.get("id_owner");

        System.out.println("idOwner = " + idOwner);
        System.out.println("idPurchase = " + idPurchase);

        System.out.println("-----SALES API-----");

        return ResponseEntity.ok(request.toString());
    }
}
