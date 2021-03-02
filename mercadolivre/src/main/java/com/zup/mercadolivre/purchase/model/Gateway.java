package com.zup.mercadolivre.purchase.model;

public interface Gateway {

    String submitPurchase(Purchase purchase);
}
