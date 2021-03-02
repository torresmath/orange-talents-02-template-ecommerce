package com.zup.mercadolivre.product.controller.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zup.mercadolivre.product.model.ProductOpinion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@JsonPropertyOrder({
        "amount",
        "average",
        "opinions"
})
public class ProductViewOpinionsResponse {

    private final Set<Map<String, String>> opinions;
    private final BigDecimal average;

    public ProductViewOpinionsResponse(Set<ProductOpinion> opinions) {
        this.opinions = opinions.stream()
                .map(o -> Map.of(
                        "title", o.getTitle(),
                        "description", o.getDescription()
                )).collect(Collectors.toSet());

        double avg = opinions.stream()
                .mapToInt(ProductOpinion::getRating)
                .average()
                .orElse(0);

        this.average = BigDecimal.valueOf(avg).setScale(2, RoundingMode.UP);
    }

    public Set<Map<String, String>> getOpinions() { return opinions; }

    public BigDecimal getAverage() { return average; }

    public int getAmount() {
        return opinions.size();
    }
}
