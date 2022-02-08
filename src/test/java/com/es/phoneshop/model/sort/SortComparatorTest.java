package com.es.phoneshop.model.sort;

import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertNotNull;

public class SortComparatorTest {

    private SortComparator sortComparator;

    @Before
    public void setUp() {
        sortComparator = new SortComparator();
    }

    @Test
    public void sortFieldAndSortOrderGetProductComparatorNotNull() {
        SortField sortField = SortField.DESCRIPTION;
        SortOrder sortOrder = SortOrder.ASC;

        Comparator<Product> productComparator = sortComparator.getSortFieldOrderComparator(sortField, sortOrder);

        assertNotNull(productComparator);
    }
}