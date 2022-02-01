package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private Currency usd;


    @Before
    public void setup() {
        usd = Currency.getInstance("USD");
        productDao = ArrayListProductDao.getInstance();
        SampleProduct.createSampleProductsArrayList(productDao);
    }

    @Test
    public void testFindProductsHaveResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProductNotNull() {
        final long ID = 4L;

        assertNotNull(productDao.getProduct(ID));
    }

    @Test
    public void testGetProductsEquals() {
        final long ID = 9L;
        final String PRODUCT_CODE = "nokia3310";

        Product product = productDao.getProduct(ID);

        assertEquals(PRODUCT_CODE, product.getCode());
    }

    @Test
    public void testRenameProductsWithSameId() {
        Product productFromList = new Product(12L, "htces4g"
                , "HTC EVO Shift 4G"
                , new BigDecimal(320)
                , usd
                , 3
                , "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/" +
                "HTC/HTC%20EVO%20Shift%204G.jpg");
        Product newProductWithSameId = new Product(12L, "otherPhone"
                , "otherPhone"
                , new BigDecimal(80)
                , usd
                , 30
                , "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/" +
                "Siemens/Siemens%20C61.jpg");

        productDao.save(newProductWithSameId);

        assertNotEquals(productFromList, productDao.getProduct(12L));
    }

    @Test(expected = NoSuchElementException.class)
    public void testSuccessDeleteProduct() {
        final long ID = 6L;

        productDao.delete(ID);

        assertNull(productDao.getProduct(ID));
    }

    @Test
    public void testSaveProductWithDifferentId() {
        Product product = new Product(32L
                , "simsxg75"
                , "Siemens SXG75"
                , new BigDecimal(150)
                , usd
                , 40
                , "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/" +
                "Siemens/Siemens%20SXG75.jpg");

        productDao.save(product);

        assertEquals(productDao.getProduct(32L), product);
    }

    @Test
    public void testFindProductsByQuery() {
        final String QUERY = "Sam";

        List<Product> productsByQuery = productDao.findProductsByQuery(QUERY);

        assertFalse(productsByQuery.isEmpty());
    }

    @Test
    public void testArrayListProductDaoIsSingleton() {
        ProductDao productDaoFirst = ArrayListProductDao.getInstance();
        ProductDao productDaoSecond = ArrayListProductDao.getInstance();

        assertSame(productDaoFirst, productDaoSecond);
    }

    @Test
    public void testProductGetByCode() {
        final String PRODUCT_CODE = "iphone";

        Product iPhone = productDao.getProduct(PRODUCT_CODE);

        assertEquals(PRODUCT_CODE, iPhone.getCode());
    }
}
