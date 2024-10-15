package io.pbhuyan.product.controller;

import io.pbhuyan.product.exception.ProductServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<String> handleProductServiceException(ProductServiceException pse){
        return ResponseEntity.status(pse.getStatusCode()).body(pse.getMessage());
    }


    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<String> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException pse){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(pse.getMessage());
    }


}
