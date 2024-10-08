package io.pbhuyan.product.exception;

import org.apache.http.HttpStatus;

public class ProductNotFoundException extends ProductServiceException{
    public ProductNotFoundException (String message){
        super(message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.SC_NOT_FOUND;
    }
}
