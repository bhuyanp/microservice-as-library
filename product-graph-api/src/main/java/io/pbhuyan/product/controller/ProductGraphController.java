package io.pbhuyan.product.controller;

import io.pbhuyan.product.entity.Product;
import io.pbhuyan.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ProductGraphController {
    private final ProductService productService;

    @QueryMapping("allProducts")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @QueryMapping("productById")
    public Product getProductById(@Argument String productId){
        return productService.getById(productId);
    }

}
