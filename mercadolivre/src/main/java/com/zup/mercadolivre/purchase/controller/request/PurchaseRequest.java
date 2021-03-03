package com.zup.mercadolivre.purchase.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.mercadolivre.common.annotations.EnumValue;
import com.zup.mercadolivre.common.annotations.ExistsId;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.purchase.model.enums.PaymentGateway;
import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.user.model.User;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class PurchaseRequest {

    @ExistsId(domainClass = Product.class)
    @JsonProperty("id_product")
    private final Long idProduct;

    @Min(1)
    private final int amount;

    @NotBlank
    @EnumValue(targetEnum = PaymentGateway.class)
    private final String gateway;

    public PurchaseRequest(Long idProduct, @Min(1) int amount, String gateway) {
        this.idProduct = idProduct;
        this.amount = amount;
        this.gateway = gateway;
    }

    public Purchase toModel(User buyer, EntityManager manager) {

        Product product = manager.find(Product.class, idProduct);
        Assert.notNull(product, "Impossivel cadastrar compra sem Produto");
        Assert.notNull(buyer.getUsername(), "Impossivel cadastrar compra sem Comprador");

        PaymentGateway paymentGateway = PaymentGateway.valueOf(gateway.toUpperCase());
        return new Purchase(paymentGateway, product, amount, buyer);
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public int getAmount() {
        return amount;
    }

    public String getGateway() {
        return gateway;
    }
}
