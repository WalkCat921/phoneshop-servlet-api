package com.es.phoneshop.model.product;

import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProductDao {
    Product getProduct(Long id) throws NoSuchElementException;

    List<Product> findProducts();

    List<Product> findProductsByQuery(String query);

    List<Product> findProductsByQuerySortFieldAndOrder(String query, SortField sortField, SortOrder sortOrder);

    void save(Product product) throws NullPointerException;

    void delete(Long id) throws NoSuchElementException;
}
