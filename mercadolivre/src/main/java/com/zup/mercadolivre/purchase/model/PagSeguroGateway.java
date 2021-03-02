package com.zup.mercadolivre.purchase.model;

public class PagSeguroGateway implements Gateway {

    @Override
    public String submitPurchase(Purchase purchase) {

        System.out.println("PAGSEGURO GATEWAY");

        return "pagseguro.com?returnId=" + purchase.getIdentifier() + "&redirectUrl=";
    }
}
