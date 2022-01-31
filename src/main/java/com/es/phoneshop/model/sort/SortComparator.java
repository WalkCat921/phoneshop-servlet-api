package com.es.phoneshop.model.sort;

import com.es.phoneshop.model.product.Product;

import java.util.Comparator;

public class SortComparator {

    public Comparator<Product> getSortFieldOrderComparator(SortField sortField, SortOrder sortOrder) {
        Comparator<Product> productComparator = Comparator.comparing(product -> {
            if (sortField != null && SortField.DESCRIPTION == sortField) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if (SortOrder.DESC == sortOrder) {
            productComparator = productComparator.reversed();
        }
        return productComparator;
    }
}
