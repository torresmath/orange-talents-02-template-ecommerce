package com.zup.mercadolivre.product.model;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String link;

    @NotNull
    @ManyToOne
    private Product product;

    public ProductImage(@NotNull @NotBlank String link, @NotNull Product product) {

        Assert.isTrue(product != null, "Impossível criar imagem sem produto");

        this.link = link;
        this.product = product;
    }

    public String getLink() {
        return link;
    }

    public Product getProduct() {
        return product;
    }

    @Deprecated
    public ProductImage() { }

    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", product=" + product +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductImage that = (ProductImage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        return product != null ? product.equals(that.product) : that.product == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + product.hashCode();
        return result;
    }
}
