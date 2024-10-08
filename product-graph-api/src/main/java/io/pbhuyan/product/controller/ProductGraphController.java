package io.pbhuyan.product.controller;

import io.pbhuyan.product.dto.ProductRequest;
import io.pbhuyan.product.dto.ProductResponse;
import io.pbhuyan.product.entity.Product;
import io.pbhuyan.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts().stream().map(ProductResponse::new).toList();
    }

    @QueryMapping("productById")
    public ProductResponse getProductById(@Argument String productId){
        Product product = productService.getById(productId);
        return new ProductResponse(product);
    }

    @MutationMapping("addProduct")
    public ProductResponse addProduct(@Argument ProductRequest productRequest){
        Product newlyAddedProduct = productService.add(productRequest.getProductEntity());
        return new ProductResponse(newlyAddedProduct);
    }

    @MutationMapping("deleteProductById")
    public String deleteProductById(@Argument String productId){
        productService.delete(productId);
        return "Product deleted successfully";
    }

}
