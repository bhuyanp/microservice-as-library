package io.pbhuyan.product.dto;

import io.pbhuyan.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse extends ProductRequest {
    private String id;

    public ProductResponse(Product product){
        this.id = product.getId();
        setTitle(product.getTitle());
        setDescription(product.getDescription());
        setPrice(product.getPrice());
    }
}
