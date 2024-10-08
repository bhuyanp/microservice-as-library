package io.pbhuyan.product.controller;

import io.pbhuyan.product.dto.ProductRequestDto;
import io.pbhuyan.product.entity.Product;
import io.pbhuyan.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest/products")
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getById(productId));
    }

    @PostMapping
    public ResponseEntity<Product> getProductById(@RequestBody @Valid ProductRequestDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.add(Product.builder()
                        .title(productDto.getTitle())
                        .description(productDto.getDescription())
                        .price(productDto.getPrice())
                .build()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteById(@PathVariable String productId) {
        productService.delete(productId);
        return ResponseEntity.ok("Product successfully deleted");
    }


}
