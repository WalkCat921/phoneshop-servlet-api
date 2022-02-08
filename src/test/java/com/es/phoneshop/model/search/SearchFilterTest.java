package com.es.phoneshop.model.search;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.model.product.SampleProduct;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchFilterTest {
    private ProductDao productDao;
    private Currency USD;


    @Before
    public void setUp() {
        productDao = ArrayListProductDao.getInstance();
        USD = Currency.getInstance("USD");
        SampleProduct.createSampleProductsArrayList(productDao);
    }

    @Test
    public void testFilteredListByQuery() {
        List<Product> products = productDao.findProducts();
        Product iPhone = productDao.getProduct("iphone");
        Product iPhone6 = productDao.getProduct("iphone6");

        products = SearchFilter.getFilteredListByQuery(products, "Apple iPhone");

        assertEquals(products.get(0), iPhone);
        assertEquals(products.get(1), iPhone6);
    }
}