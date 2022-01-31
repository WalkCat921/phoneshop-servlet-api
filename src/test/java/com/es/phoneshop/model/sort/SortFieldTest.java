package com.es.phoneshop.model.sort;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SortFieldTest {

    @Test
    public void testSortFieldGetDescription() {
        String sortStringField = "DeScrIptIoN";

        SortField sortField = SortField.getSortFieldByRequestParam(sortStringField);

        assertEquals(sortField, SortField.DESCRIPTION);
    }

    @Test
    public void testSortFieldGetPrice() {
        String sortStringField = "priCe";

        SortField sortField = SortField.getSortFieldByRequestParam(sortStringField);

        assertEquals(sortField, SortField.PRICE);
    }
}