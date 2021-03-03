package com.zup.mercadolivre.purchase.controller.request;

import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.purchase.repository.PurchaseRepository;

/*
todo Aplicar composição e transformar a implementação dessa interface em um objeto utilizado pelos DTOs de Request
*/
public interface ValidTransactionRequest {

    void validatePurchase(PurchaseRepository repository);
    Purchase getPurchase();
}
