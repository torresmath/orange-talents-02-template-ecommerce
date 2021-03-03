package com.zup.mercadolivre.purchase.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.mercadolivre.common.annotations.EnumValue;
import com.zup.mercadolivre.purchase.model.Transaction;
import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.purchase.model.enums.PagseguroStatus;
import com.zup.mercadolivre.purchase.repository.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public class PagseguroTransactionRequest implements ValidTransactionRequest, ToTransaction {

    @JsonProperty("id_gateway_purchase")
    @NotBlank
    private final String idGatewayTransaction;

    @NotBlank
    private final String identifier;

    @EnumValue(targetEnum = PagseguroStatus.class)
    private final String status;

    @JsonIgnore
    private Purchase purchase;

    public PagseguroTransactionRequest(@NotBlank String idGatewayTransaction, @NotBlank String identifier, String status) {
        this.idGatewayTransaction = idGatewayTransaction;
        this.identifier = identifier;
        this.status = status;
    }

    public Transaction toTransaction() {

        Assert.notNull(purchase, "Impossivel prosseguir pagamento com Compra invalida");

        PagseguroStatus pagStatus = PagseguroStatus.valueOf(status);
        return new Transaction(idGatewayTransaction, purchase, pagStatus.getTransactionStatus());
    }

    public String getIdGatewayTransaction() {
        return idGatewayTransaction;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void validatePurchase(PurchaseRepository repository) {

        Optional<Purchase> possiblePurchase = repository.findByIdentifier(identifier);

        if (possiblePurchase.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "purchase.not_found");
        }

        Purchase purchase = possiblePurchase.get();
        if (purchase.isDone()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "purchase.is_done");
        }

        this.purchase = purchase;
    }

    public Purchase getPurchase() {
        return purchase;
    }
}
