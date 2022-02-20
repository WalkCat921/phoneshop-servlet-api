package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProductDao extends DAO<Product> {
    Product getByCode(String code) throws NoSuchElementException, NullPointerException;

    List<Product> findProductsByQuery(String query);

    void updateStock(Order order);

    List<Product> findProductsByQuerySortFieldAndOrder(String query, SortField sortField, SortOrder sortOrder);
}
