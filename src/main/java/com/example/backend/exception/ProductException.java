package com.example.backend.exception;

public class ProductException extends BaseException{


    public ProductException(String code) {
        super("Product."+code);
    }

    public static ProductException notFound(){
        return new ProductException("not.Found");
    }
}
