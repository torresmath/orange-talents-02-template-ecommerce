package com.zup.mercadolivre.purchase.model;

import org.springframework.web.util.UriComponentsBuilder;

public class PaypalGateway implements Gateway {
    @Override
    public String submitPurchase(Purchase purchase) {
        System.out.println("PAYPAL GATEWAY");

        String redirectUri = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost:8080")
                .path("/purchase/{id}")
                .buildAndExpand(purchase.getIdentifier()).toUriString();

        System.out.println("redirectUri = " + redirectUri);
        return "paypal.com/" + purchase.getIdentifier() + "?redirect=" + redirectUri;
    }
}
