package com.zup.mercadolivre.purchase.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.purchase.model.Transaction;
import com.zup.mercadolivre.purchase.model.enums.PaypalStatus;
import com.zup.mercadolivre.purchase.repository.PurchaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class PaypalTransactionRequest implements ValidTransactionRequest, ToTransaction {

    @JsonProperty("id_gateway_purchase")
    @NotBlank
    private final String idGatewayTransaction;

    @NotBlank
    private final String identifier;

    @Min(0)
    @Max(1)
    @NotNull
    private final Integer status;

    @JsonIgnore
    private Purchase purchase;

    public PaypalTransactionRequest(@NotBlank String idGatewayTransaction, @NotBlank String identifier, Integer status) {
        this.idGatewayTransaction = idGatewayTransaction;
        this.identifier = identifier;
        this.status = status;
    }

    public String getIdGatewayTransaction() {
        return idGatewayTransaction;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public Transaction toTransaction() {

        Assert.notNull(purchase, "Impossivel prosseguir pagamento com Compra invalida");

        PaypalStatus paypalStatus = PaypalStatus.getStatus(status);
        return new Transaction(idGatewayTransaction, purchase, paypalStatus.getTransactionStatus());
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

    @Override
    public Purchase getPurchase() {
        return purchase;
    }

}
