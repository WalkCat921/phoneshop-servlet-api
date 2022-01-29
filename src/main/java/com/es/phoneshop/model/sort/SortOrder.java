package com.es.phoneshop.model.sort;

public enum SortOrder {
    ASC, DESC;

    public static SortOrder getSortOrderByRequestParam(String sortOrder) {
        if (sortOrder == null || sortOrder.isEmpty()) {
            return null;
        } else if (SortOrder.valueOf(sortOrder.toUpperCase()) == ASC) {
            return ASC;
        } else if (SortOrder.valueOf(sortOrder.toUpperCase()) == DESC) {
            return DESC;
        } else {
            return null;
        }
    }
}
