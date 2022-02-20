package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.search.SearchFilter;
import com.es.phoneshop.model.sort.SortComparator;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractDao<Product> implements ProductDao {

    private static volatile ProductDao instance;

    private ArrayListProductDao() {
    }

    public static ProductDao getInstance() {
        if (instance == null) {
            synchronized (ProductDao.class) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Product> findAll() {
        return super.findAll().stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public Product getByCode(String code) throws NoSuchElementException, NullPointerException {
        synchronized (LOCK) {
            return super.findAll().stream()
                    .filter(product -> code.equals(product.getCode()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException(String.format("Product with code %s not found", code)));
        }
    }

    @Override
    public List<Product> findProductsByQuery(String query) {
        synchronized (LOCK) {
            if (query != null && !query.trim().isEmpty()) {
                return SearchFilter.getFilteredListByQuery(findAll(), query);
            } else {
                return findAll();
            }
        }
    }

    @Override
    public void updateStock(Order order) {
        order.getItemList().stream().forEach(cartItem -> {
            findAll().stream().forEach(product -> {
                if (cartItem.getProduct().getCode().equals(product.getCode())) {
                    product.setStock(product.getStock() - cartItem.getQuantity());
                }
            });
        });
    }

    @Override
    public List<Product> findProductsByQuerySortFieldAndOrder(String query, SortField sortField, SortOrder sortOrder) {
        synchronized (LOCK) {
            SortComparator sortComparator = new SortComparator();
            return findProductsByQuery(query).stream()
                    .sorted(sortComparator.getSortFieldOrderComparator(sortField, sortOrder))
                    .collect(Collectors.toList());
        }
    }
}