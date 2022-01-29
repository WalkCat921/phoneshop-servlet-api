package com.es.phoneshop.model.product;

import com.es.phoneshop.model.search.SearchFilter;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.ArrayList;
import java.util.Comparator;
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
    public Product getProduct(Long id) throws NoSuchElementException {
        synchronized (LOCK) {
            if (id == null) {
                return null;
            }
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .get();
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
            Comparator<Product> productComparator = Comparator.comparing(product -> {
                if (sortField != null && SortField.DESCRIPTION == sortField) {
                    return (Comparable) product.getDescription();
                } else {
                    return (Comparable) product.getPrice();
                }
            });
            if (SortOrder.DESC == sortOrder) {
                productComparator = productComparator.thenComparing(productComparator).reversed();
            }
            return findProductsByQuery(query).stream().sorted(productComparator).collect(Collectors.toList());
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
                                .findAny()
                                .get());
            }
        }
    }
}
