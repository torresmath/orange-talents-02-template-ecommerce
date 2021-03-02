package com.zup.mercadolivre.purchase.model;

import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.user.model.User;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String identifier;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(columnDefinition = "enum('PAGSEGURO', 'PAYPAL')")
    private PaymentGateway gateway;

    @ManyToOne
    @NotNull
    private Product product;

    @Min(1)
    @NotNull
    private int amount;

    @ManyToOne
    @NotNull
    private User buyer;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PurchaseStatus status;

    public Purchase(@NotNull PaymentGateway gateway,
                    @NotNull Product product,
                    @Min(1) @NotNull int amount,
                    @NotNull User buyer) {

        Assert.notNull(product, "Impossivel cadastrar compra sem Produto");
        Assert.state(buyer.getUsername() != null && !buyer.getUsername().isBlank(), "Impossivel cadastrar compra sem Comprador");
        Assert.state(product.hasAmount(amount), "Impossível cadastrar compra com a quantidade solicitada: " + amount);

        this.gateway = gateway;
        this.product = product;
        this.amount = amount;
        this.buyer = buyer;

        this.identifier = UUID.randomUUID().toString();
        this.status = PurchaseStatus.IN_PROGRESS;

        this.product.decreaseAmount(amount);
    }

    @Deprecated
    public Purchase() {}

    public PaymentGateway getGateway() {
        return gateway;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String submitPurchase() {
        return this.gateway.getGateway().submitPurchase(this);
    }

    public String getOwnerEmail() {
        return product.getOwnerEmail();
    }

    public String getBuyerEmail() {
        return buyer.getLogin();
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public Product getProduct() {
        return product;
    }
}
