package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private final Object LOCK = new Object();

    private List<Product> products;
    private long idCounter;

    public ArrayListProductDao() {
        this.products = new ArrayList<>();
        SampleProduct sampleProduct = new SampleProduct(this);
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
    public void save(Product product) {
        synchronized (LOCK) {
            if (product == null) {
                return;
            }
            if (product.getId() != null) {
                for (Product productFromList : products) {
                    if (product.getId().equals(productFromList.getId())) {
                        products.set(products.indexOf(productFromList), product);
                        return;
                    }
                }
                products.add(product);
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
