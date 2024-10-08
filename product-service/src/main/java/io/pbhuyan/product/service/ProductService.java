package io.pbhuyan.product.service;

import io.pbhuyan.product.entity.Product;
import io.pbhuyan.product.exception.InvalidProductException;
import io.pbhuyan.product.exception.ProductNotFoundException;
import io.pbhuyan.product.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final Validator validator;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getById(final String productId) {
        return productRepo.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Not able to find any product with id " + productId));
    }
    public Product add(final Product product) {
        validateProduct(product);
        return productRepo.save(product);
    }

    void validateProduct(Product product) {
        Errors errors = validator.validateObject(product);
        if(errors.hasErrors()){
            throw new InvalidProductException(errors.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString());
        }
    }

    public void delete(final String productId) {
        productRepo.deleteById(productId);
    }
}
