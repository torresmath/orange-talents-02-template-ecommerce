package com.zup.mercadolivre.purchase.model;

import org.springframework.web.util.UriComponentsBuilder;

public class PagSeguroGateway implements Gateway {

    @Override
    public String submitPurchase(Purchase purchase) {

        System.out.println("PAGSEGURO GATEWAY");

        String redirectUri = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost:8080")
                .path("/purchase/{id}")
                .buildAndExpand(purchase.getIdentifier()).toUriString();

        return "pagseguro.com?returnId=" + purchase.getIdentifier() + "&redirectUrl=" + redirectUri;
    }
}
