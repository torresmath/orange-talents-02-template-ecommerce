package com.zup.mercadolivre.purchase.controller.response;

import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.purchase.model.enums.PurchaseStatus;

public class PurchaseResponse {

    private final String identifier;
    private final PurchaseStatus status;

    public PurchaseResponse(Purchase purchase) {
        this.identifier = purchase.getIdentifier();
        this.status = purchase.getStatus();
    }

    public String getIdentifier() {
        return identifier;
    }

    public PurchaseStatus getStatus() {
        return status;
    }
}
