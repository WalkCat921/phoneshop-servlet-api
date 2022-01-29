package com.es.phoneshop.model.product;

import com.es.phoneshop.model.search.SearchFilter;
import com.es.phoneshop.model.sort.SortComparator;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private final Object LOCK = new Object();
    private static volatile ProductDao instance;

    private List<Product> products;
    private long idCounter;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    public static ProductDao getInstance() {
        ProductDao localInstance = instance;
        if (localInstance == null) {
            synchronized (ProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArrayListProductDao();
                }
            }
        }
        return localInstance;
    }

    @Override
    public Product getProduct(Long id) throws NoSuchElementException, NullPointerException {
        synchronized (LOCK) {
            if (id == null) {
                throw new NullPointerException("id is null!");
            }
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny().orElseThrow(() -> new NoSuchElementException("Product with ID " + id + " not found"));
        }
    }

    @Override
    public Product getProduct(String code) throws NoSuchElementException, NullPointerException {
        synchronized (LOCK) {
            if (code == null || code.isEmpty()) {
                throw new NullPointerException("Code is null or empty");
            }
            return products.stream()
                    .filter(product -> code.equals(product.getCode()))
                    .findAny().orElseThrow(() -> new NoSuchElementException("Product with code " + code + " not found"));
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
            SearchFilter searchFilter = new SearchFilter();
            if (query != null && !query.trim().isEmpty()) {
                return searchFilter.getFilteredListByQuery(findProducts(), query);
            } else {
                return findProducts();
            }
        }
    }

    @Override
    public List<Product> findProductsByQuerySortFieldAndOrder(String query, SortField sortField, SortOrder sortOrder) {
        synchronized (LOCK) {
            SortComparator sortComparator = new SortComparator();
            return findProductsByQuery(query).stream().sorted(sortComparator.getSortFieldOrderComparator(sortField, sortOrder)).collect(Collectors.toList());
        }
    }


    @Override
    public void save(Product product) throws NullPointerException {
        synchronized (LOCK) {
            if (product == null) {
                throw new NullPointerException("Product is null");
            }
            if (product.getId() != null) {
                try {
                    Product productWithSameId = products.stream()
                            .filter(p -> p.getId().equals(product.getId()))
                            .findAny()
                            .get();
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
                products.remove(
                        products.stream()
                                .filter(product -> product.getId().equals(id))
                                .findAny().orElseThrow(() -> new NoSuchElementException("Product with ID " + id + " not found to delete")));
            }
        }
    }
}
