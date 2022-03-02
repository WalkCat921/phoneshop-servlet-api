package com.es.phoneshop.model.search;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SearchFilter {

    private static final Object LOCK = new Object();

    private static ProductDao productDao = ArrayListProductDao.getInstance();

    public static List<Product> getFilteredListByQuery(@NonNull List<Product> products, String query) throws NullPointerException {
        synchronized (LOCK) {
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

    public static List<Product> getListBySearchFilters(String productCode, int minStock,
                                                       BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productDao.findAll();
        if (productCode != null && !productCode.isEmpty()) {
            products = findProductsByFilterProductCode(products, productCode);
        }
        if (minStock >= 0) {
            products = findProductsByFilterMinStock(products, minStock);
        }
        if (minPrice != null) {
            products = findProductsByFilterMinPrice(products, minPrice);
        }
        if (maxPrice != null) {
            products = findProductsByFilterMaxPrice(products, maxPrice);
        }
        return products;
    }

    private static List<Product> findProductsByFilterMinPrice(List<Product> products, BigDecimal minPrice) {
        return products.stream()
                .filter(product -> product.getPrice().compareTo(minPrice) >= 0)
                .collect(Collectors.toList());
    }

    private static List<Product> findProductsByFilterMaxPrice(List<Product> products, BigDecimal maxPrice) {
        return products.stream()
                .filter(product -> product.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    private static List<Product> findProductsByFilterMinStock(List<Product> products, int minStock) {
        return products.stream()
                .filter(product -> product.getStock() >= minStock)
                .collect(Collectors.toList());
    }

    private static List<Product> findProductsByFilterProductCode(List<Product> products, String productCode) {
        return products.stream()
                .filter(product -> product.getCode().equals(productCode))
                .collect(Collectors.toList());
    }

}