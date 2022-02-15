package com.es.phoneshop.exception;

public class OutOfStockException extends Exception {

    private final int stockAvailable;

    public OutOfStockException(int stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public String getMessage() {
        return String.format("Out of stock. Available stock is %d", stockAvailable);
    }
}