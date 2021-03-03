package com.zup.mercadolivre.purchase.model;

import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.purchase.model.enums.PaymentGateway;
import com.zup.mercadolivre.purchase.model.enums.PurchaseStatus;
import com.zup.mercadolivre.user.model.User;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.MERGE)
    private final Set<Transaction> transactions = new HashSet<>();

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

    public void addTransaction(Transaction transaction) {
        Assert.isTrue(!containsTransaction(transaction), "Já existe uma transacao igual a essa cadastrada");
        Assert.isTrue(!containsSuccessfulTransactions(), "Essa compra ja foi concluida com sucesso");

        this.transactions.add(transaction);

        if (transaction.successful()) {
            finish();
        }
    }

    void finish() {
        Assert.state(!this.isDone(), "Impossivel atualizar Status da compra, a mesma ja foi finalizada");

        this.status = PurchaseStatus.DONE;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Purchase purchase = (Purchase) o;

        if (amount != purchase.amount) return false;
        if (id != null ? !id.equals(purchase.id) : purchase.id != null) return false;
        if (identifier != null ? !identifier.equals(purchase.identifier) : purchase.identifier != null) return false;
        if (gateway != purchase.gateway) return false;
        if (product != null ? !product.equals(purchase.product) : purchase.product != null) return false;
        if (buyer != null ? !buyer.equals(purchase.buyer) : purchase.buyer != null) return false;
        if (status != purchase.status) return false;
        return transactions != null ? transactions.equals(purchase.transactions) : purchase.transactions == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        result = 31 * result + (gateway != null ? gateway.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + amount;
        result = 31 * result + (buyer != null ? buyer.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        return result;
    }

    public boolean isDone() {
        return this.status.equals(PurchaseStatus.DONE);
    }

    public boolean containsTransaction(Transaction transaction) {
        return this.transactions.contains(transaction);
    }

    public boolean containsSuccessfulTransactions() {
        Set<Transaction> successfulTransactions = this.transactions.stream().filter(Transaction::successful).collect(Collectors.toSet());

        return !successfulTransactions.isEmpty();
    }
}
