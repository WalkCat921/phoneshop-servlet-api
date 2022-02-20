package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

import java.util.NoSuchElementException;

public interface OrderDao extends DAO<Order> {
    Order getBySecureId(String secureId) throws NoSuchElementException, NullPointerException;
}
