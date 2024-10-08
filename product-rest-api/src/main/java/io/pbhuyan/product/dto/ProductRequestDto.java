package io.pbhuyan.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequestDto {
    private String id;
    @NotBlank(message = "Product title is required")
    private String title;
    private String description;
    @Positive(message = "Price must be a positive value")
    @NotNull(message = "Product price is required")
    private BigDecimal price;
}
