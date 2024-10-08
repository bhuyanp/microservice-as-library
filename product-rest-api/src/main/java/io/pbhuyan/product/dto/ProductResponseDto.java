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
public class ProductResponseDto extends ProductRequestDto {
    private String id;

    public ProductResponseDto(Product product){
        this.id = product.getId();
        setTitle(product.getTitle());
        setDescription(product.getDescription());
        setPrice(product.getPrice());
    }
}
