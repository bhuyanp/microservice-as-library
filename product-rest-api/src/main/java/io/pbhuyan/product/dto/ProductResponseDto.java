package io.pbhuyan.product.dto;

import io.pbhuyan.product.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductResponseDto {
    private String id;
    private String title;
    private String description;
    private BigDecimal price;

    public ProductResponseDto(Product product){
        setId(product.getId());
        setTitle(product.getTitle());
        setDescription(product.getDescription());
        setPrice(product.getPrice());
    }
}
