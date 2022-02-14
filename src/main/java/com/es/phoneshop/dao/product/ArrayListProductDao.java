package com.es.phoneshop.dao.product;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.search.SearchFilter;
import com.es.phoneshop.model.sort.SortComparator;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static final Object LOCK = new Object();
    private static volatile ProductDao instance;

    private List<Product> products;
    private long idCounter;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
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
    public Product getProduct(@NonNull Long id) throws NoSuchElementException, NullPointerException {
        synchronized (LOCK) {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException(String.format("Product with ID %n not found", id)));
        }
    }

    @Override
    public Product getProduct(@NonNull String code) throws NoSuchElementException, NullPointerException {
        synchronized (LOCK) {
            return products.stream()
                    .filter(product -> code.equals(product.getCode()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException(String.format("Product with code %s not found", code)));
        }
    }

    @Override
    public List<Product> findProducts() {
        synchronized (LOCK) {
            return products.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Product> findProductsByQuery(String query) {
        synchronized (LOCK) {
            if (query != null && !query.trim().isEmpty()) {
                return SearchFilter.getFilteredListByQuery(findProducts(), query);
            } else {
                return findProducts();
            }
        }
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

    @Override
    public void save(@NonNull Product product) throws NullPointerException {
        synchronized (LOCK) {
            if (product.getId() != null) {
                try {
                    Product productWithSameId = getProduct(product.getId());
                    products.set(products.indexOf(productWithSameId), product);
                } catch (NoSuchElementException ex) {
                    products.add(product);
                }
            } else {
                product.setId(++idCounter);
                products.add(product);
            }
        }
    }

    @Override
    public void delete(Long id) throws NoSuchElementException {
        synchronized (LOCK) {
            if (id != null) {
                products.remove(getProduct(id));
            }
        }
    }
}