package io.pbhuyan.product.exception;

import org.apache.http.HttpStatus;

public abstract class ProductServiceException extends RuntimeException {
    public ProductServiceException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

}
