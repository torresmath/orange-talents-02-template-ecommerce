package com.zup.mercadolivre.purchase.model;

import com.zup.mercadolivre.purchase.model.enums.TransactionStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String idGatewayPurchase;

    @ManyToOne
    @NotNull
    private Purchase purchase;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(columnDefinition = "enum('SUCCESS', 'ERROR')")
    private TransactionStatus status;

    @NotNull
    @CreationTimestamp
    private LocalDateTime createDate;

    public Transaction(String idGatewayPurchase, @NotNull Purchase purchase, @NotNull TransactionStatus status) {
        this.idGatewayPurchase = idGatewayPurchase;
        this.purchase = purchase;
        this.status = status;

        this.createDate = LocalDateTime.now();
    }

    public Purchase getPurchase() { return purchase; }

    public String getStatus() { return this.status.name(); }

    @Deprecated
    public Transaction() {}

    public boolean successful() {
        return this.status.equals(TransactionStatus.SUCCESS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction transaction = (Transaction) o;

        return idGatewayPurchase != null ? idGatewayPurchase.equals(transaction.idGatewayPurchase) : transaction.idGatewayPurchase == null;
    }

    @Override
    public int hashCode() {
        return idGatewayPurchase != null ? idGatewayPurchase.hashCode() : 0;
    }
}
