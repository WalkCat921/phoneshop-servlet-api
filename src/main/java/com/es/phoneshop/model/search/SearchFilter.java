package com.es.phoneshop.model.search;

import com.es.phoneshop.model.product.Product;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SearchFilter {

    private static final Object LOCK = new Object();

    public static List<Product> getFilteredListByQuery(List<Product> products, String query) throws NullPointerException {
        synchronized (LOCK) {
            if (query == null || products == null) {
                throw new NullPointerException();
            }
            String[] wordOfQuery = query.toLowerCase(Locale.ROOT).split("\\s+");
            Function<Product, Long> countMatchesProductsFunction = product -> Arrays.stream(wordOfQuery)
                    .filter(queryStream -> product.getDescription().toLowerCase(Locale.ROOT).contains(queryStream))
                    .count();

            return products
                    .stream()
                    .filter(product -> countMatchesProductsFunction.apply(product) != 0)
                    .sorted(Comparator.comparing(countMatchesProductsFunction).reversed())
                    .collect(Collectors.toList());
        }
    }
}