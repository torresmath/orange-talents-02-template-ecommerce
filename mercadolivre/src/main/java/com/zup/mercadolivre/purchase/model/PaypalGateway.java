package com.zup.mercadolivre.purchase.model;

public class PaypalGateway implements Gateway{
    @Override
    public String submitPurchase(Purchase purchase) {
        System.out.println("PAYPAL GATEWAY");

        return "paypal.com/" + purchase.getIdentifier() + "?redirect";
    }
}
