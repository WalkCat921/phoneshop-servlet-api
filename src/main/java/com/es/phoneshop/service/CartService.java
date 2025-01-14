package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpSession;

public interface CartService {
    Cart getCart(HttpSession session);

    void add(Cart cart, String productCode, int quantity) throws OutOfStockException, IllegalArgumentException, NullPointerException;

    void update(Cart cart, String productCode, int quantity) throws OutOfStockException, IllegalArgumentException, NullPointerException;

    void delete(Cart cart, String productCode);

    void clear(Cart cart);
}