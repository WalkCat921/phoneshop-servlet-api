package com.es.phoneshop.model.sort;

public enum SortField {
    DESCRIPTION, PRICE;

    public static SortField getSortFieldByRequestParam(String sortField) {
        if (sortField == null || sortField.isEmpty()) {
            return null;
        } else if (SortField.valueOf(sortField.toUpperCase()) == DESCRIPTION) {
            return DESCRIPTION;
        } else if (SortField.valueOf(sortField.toUpperCase()) == PRICE) {
            return PRICE;
        } else {
            return null;
        }
    }
}
