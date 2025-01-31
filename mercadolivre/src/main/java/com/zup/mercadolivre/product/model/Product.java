package com.zup.mercadolivre.product.model;

import com.zup.mercadolivre.category.model.Category;
import com.zup.mercadolivre.user.model.User;
import io.jsonwebtoken.lang.Assert;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    @Column(columnDefinition = "Decimal(10,2)")
    @DecimalMin("0.01")
    BigDecimal price;

    @Min(0)
    int amount;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 1000)
    String description;

    @ManyToOne
    @NotNull
    Category category;

    @ManyToOne
    @NotNull
    User owner;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    List<ProductDetail> details;

    @OneToMany(mappedBy = "product")
    Set<ProductOpinion> opinions;

    @OneToMany(mappedBy = "product")
    @OrderBy("title asc")
    SortedSet<ProductQuestion> questions = new TreeSet<>();

    @OneToMany(mappedBy = "product")
    List<ProductImage> images;

    @CreationTimestamp
    private LocalDateTime createDate;

    public Product(@NotNull @NotBlank String name,
                   @NotNull @DecimalMin("0.01") BigDecimal price,
                   @Min(0) int amount,
                   @NotNull @NotBlank @Length(min = 1, max = 1000) String description,
                   @NotNull Category category,
                   @NotNull User owner) {

        this.name = name;
        this.price = price;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.owner = owner;
    }

    public Product(@NotNull @NotBlank String name,
                   @NotNull @DecimalMin("0.01") BigDecimal price,
                   @Min(0) int amount,
                   @NotNull @NotBlank @Length(min = 1, max = 1000) String description,
                   @NotNull Category category,
                   @NotNull User owner,
                   @NotEmpty List<ProductDetail> details) {

        this.name = name;
        this.price = price;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.owner = owner;
        this.details = details;
    }

    public User getOwner() {
        return owner;
    }

    public String getOwnerEmail() {
        return owner.getLogin();
    }

    public String getName() {
        return name;
    }

    public int getAmount() { return amount; }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public <T> Set<T> mapDetails(Function<ProductDetail, T> mapper) {
        return this.details.stream().map(mapper).collect(Collectors.toSet());
    }

    public <T> Set<T> mapOpinions(Function<ProductOpinion, T> mapper) {
        return this.opinions.stream().map(mapper).collect(Collectors.toSet());
    }

    public Set<ProductOpinion> getOpinions() {
        return Collections.unmodifiableSet(this.opinions);
    }

    public <T extends Comparable<T>> SortedSet<T> mapQuestions(Function<ProductQuestion, T> mapper) {
        return this.questions.stream().map(mapper).collect(Collectors.toCollection(TreeSet::new));
    }

    public <T> Set<T> mapImages(Function<ProductImage, T> mapper) {
        return this.images.stream().map(mapper).collect(Collectors.toSet());
    }

    public void decreaseAmount(int amount) {
        Assert.isTrue(amount > 0, "A quantidade a decrementar deve ser maior que zero. Valor: " + amount);

        if (hasAmount(amount)) {
            this.amount -= amount;
        }
    }

    @Deprecated
    public Product() {
    }

    public boolean outOfStock() {
        return this.amount == 0;
    }

    public boolean hasAmount(int amount) {
        return this.amount >= amount;
    }

    public Long getOwnerId() { return owner.getId(); }
}
