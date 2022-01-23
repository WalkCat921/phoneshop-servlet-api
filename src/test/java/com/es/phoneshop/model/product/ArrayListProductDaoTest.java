package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsHaveResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProductNotNull(){
        final long ID = 4l;
        assertNotNull(productDao.getProduct(ID));
    }

    @Test
    public void testGetProductsEquals() {
        final long ID = 9l;
        assertEquals("nokia3310", productDao.getProduct(ID).getCode());
    }

    @Test
    public void testNoSaveProductsWithSameId() {
        Product firstProduct = productDao.getProduct(12l);
        Product secondProduct = new Product(12L, "otherPhone"
                , "otherPhone"
                , new BigDecimal(80)
                , usd
                , 30
                , "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg");
        productDao.save(secondProduct);
        assertNotEquals(firstProduct, productDao.getProduct(12L));
    }

    @Test(expected = NoSuchElementException.class)
    public void testSuccessDeleteProduct() {
        productDao.delete(4l);
        assertNull(productDao.getProduct(4l));
    }

    @Test
    public void testSaveProductWithDifferentId(){
        Product product = new Product(32L,"simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        assertEquals(productDao.getProduct(32l),product);
    }
}
