package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.price.PriceHistory;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SampleProduct {

    private static final Currency USD = Currency.getInstance("USD");

    private static List<LocalDate> createLocalDateList() {
        List<LocalDate> localDates = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        localDate.datesUntil(localDate.minus(3, ChronoUnit.YEARS), Period.ofDays(-365)).forEach(id -> {
            localDates.add(id);
        });
        return localDates;
    }

    private static List<BigDecimal> createPriceList(@NonNull BigDecimal productPrice) throws NullPointerException {
        return Stream.iterate(productPrice, price -> {
            BigDecimal min = new BigDecimal(String.valueOf(price.divide(BigDecimal.valueOf(2))));
            BigDecimal max = min.add(new BigDecimal(String.valueOf(min.multiply(BigDecimal.valueOf(2)))));
            BigDecimal range = max.subtract(min);
            BigDecimal result = min.add(range.multiply(new BigDecimal(Math.random())));
            return result;
        }).limit(4).collect(Collectors.toList());
    }

    public static PriceHistory createPriceHistory(BigDecimal price) {
        return new PriceHistory(createLocalDateList(), createPriceList(price));
    }

    public static void createSampleProductsArrayList(ProductDao productDao) throws NullPointerException {
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", createPriceHistory(new BigDecimal(100))));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", createPriceHistory(new BigDecimal(200))));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", createPriceHistory(new BigDecimal(300))));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), USD, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", createPriceHistory(new BigDecimal(200))));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", createPriceHistory(new BigDecimal(1000))));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), USD, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", createPriceHistory(new BigDecimal(320))));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", createPriceHistory(new BigDecimal(420))));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", createPriceHistory(new BigDecimal(120))));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", createPriceHistory(new BigDecimal(70))));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", createPriceHistory(new BigDecimal(170))));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), USD, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", createPriceHistory(new BigDecimal(70))));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", createPriceHistory(new BigDecimal(80))));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), USD, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", createPriceHistory(new BigDecimal(150))));
    }
}