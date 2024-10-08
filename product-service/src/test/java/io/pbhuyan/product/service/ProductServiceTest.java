package io.pbhuyan.product.service;

import io.pbhuyan.product.entity.Product;
import io.pbhuyan.product.exception.InvalidProductException;
import io.pbhuyan.product.repo.ProductRepo;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.constant.Constable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepo productRepo;

    @Mock
    Validator validator;

    @InjectMocks
    ProductService productService;

    @Test
    void getAllProductsShouldReturnProducts() {
        //given
        given(productRepo.findAll()).willReturn(List.of(new Product("1", "One", "One Description",
                BigDecimal.valueOf(101.02))));
        //when
        List<Product> allProducts = productService.getAllProducts();

        //then
        verify(productRepo, times(1)).findAll();
        assertThat(allProducts).hasSize(1)
                .is(new Condition<>(p ->
                        p.getId().equals("1") &&
                                p.getTitle().equals("One") &&
                                p.getDescription().equals("One Description")
                        , ""), Index.atIndex(0));

    }

    @Test
    void getByIdShouldReturnAProduct() {
        //given
        given(productRepo.findById("1")).willReturn(Optional.of(new Product("1", "One", "One Description",
                BigDecimal.valueOf(101.02))));
        //when
        Product product = productService.getById("1");

        //then
        verify(productRepo, times(1)).findById("1");
        assertThat(product)
                .is(new Condition<>(p ->
                        p.getId().equals("1") &&
                                p.getTitle().equals("One") &&
                                p.getDescription().equals("One Description")
                        , ""));
    }

    @Test
    void add() {
    }

    @Test
    void delete() {
    }


//    @Test
//    void validateProductShouldFailWithoutTitle() {
//        //given
//        Product productWithoutTitle = new Product();
//        productWithoutTitle.setPrice(BigDecimal.valueOf(123.12));
//
//        //when
//        //then
//        assertThrows(InvalidProductException.class, () -> productService.validateProduct(productWithoutTitle));
//
//    }
//
//    @Test
//    void validateProductShouldFailWithoutPrice() {
//        //given
//        Product productWithoutPrice = new Product();
//        productWithoutPrice.setDescription("Description");
//
//        //when
//        //then
//        assertThrows(InvalidProductException.class, () -> productService.validateProduct(productWithoutPrice));
//    }
}