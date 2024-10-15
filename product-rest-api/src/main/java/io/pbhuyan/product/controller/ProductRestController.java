package io.pbhuyan.product.controller;

import io.pbhuyan.product.dto.ProductRequestDto;
import io.pbhuyan.product.dto.ProductResponseDto;
import io.pbhuyan.product.entity.Product;
import io.pbhuyan.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.pbhuyan.product.controller.ProductRestController.PRODUCT_SERVICE_BASE_URI;

@RestController
@RequestMapping(PRODUCT_SERVICE_BASE_URI)
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService productService;

    public static final String PRODUCT_SERVICE_BASE_URI = "/api/v1/rest/products";

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts()
                .stream().map(ProductResponseDto::new).toList());
    }


    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable String productId) {
        Product productById = productService.getById(productId);
        return ResponseEntity.ok(new ProductResponseDto(productById));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> getProductById(@RequestBody @Valid ProductRequestDto productDto) {
        Product newlyAddedProduct = productService.add(productDto.getProductEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponseDto(newlyAddedProduct));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteById(@PathVariable String productId) {
        productService.delete(productId);
        return ResponseEntity.ok("Product successfully deleted");
    }
}
