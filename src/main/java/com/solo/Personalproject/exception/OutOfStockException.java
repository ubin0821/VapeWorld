package com.solo.Personalproject.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message){
        super(message);
    }

}