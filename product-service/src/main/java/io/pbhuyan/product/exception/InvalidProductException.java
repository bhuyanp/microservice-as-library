package io.pbhuyan.product.exception;

import org.apache.http.HttpStatus;

public class InvalidProductException extends ProductServiceException {

    public InvalidProductException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.SC_BAD_REQUEST;
    }
}
