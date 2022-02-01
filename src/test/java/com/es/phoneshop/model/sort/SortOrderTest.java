package com.es.phoneshop.model.sort;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SortOrderTest {

    @Test
    public void StringSortOrderGetSortOrderByParamEqualsAsc() {
        String sortStringOrder = "asc";

        SortOrder sortOrder = SortOrder.getSortOrderByRequestParam(sortStringOrder);

        assertEquals(sortOrder, SortOrder.ASC);
    }

    @Test
    public void StringSortOrderGetSortOrderByParamEqualsDesc() {
        String sortStringOrder = "DeSC";

        SortOrder sortOrder = SortOrder.getSortOrderByRequestParam(sortStringOrder);

        assertEquals(sortOrder, SortOrder.DESC);
    }
}