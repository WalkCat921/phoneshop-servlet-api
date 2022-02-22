package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SampleProduct;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.OrderServiceImpl;
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
    private OrderService orderService;
    private Currency usd;

    @Before
    public void setup() {
        usd = Currency.getInstance("USD");
        productDao = ArrayListProductDao.getInstance();
        orderService = OrderServiceImpl.getInstance();
        SampleProduct.createSampleProductsArrayList(productDao);
    }

    @Test
    public void testFindProductsHaveResults() {
        assertFalse(productDao.findAll().isEmpty());
    }

    @Test
    public void testGetProductNotNull() {
        final long ID = 4L;

        assertNotNull(productDao.get(ID));
    }

    @Test
    public void testGetProductsEquals() {
        final long ID = 9L;
        final String nokia3310Code = "nokia3310";

        Product product = productDao.get(ID);

        assertEquals(nokia3310Code, product.getCode());
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

        assertNotEquals(productFromList, productDao.get(12L));
    }

    @Test(expected = NoSuchElementException.class)
    public void testSuccessDeleteProduct() {
        final long ID = 6L;

        productDao.delete(ID);

        assertNull(productDao.get(ID));
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

        assertEquals(productDao.get(32L), product);
    }

    @Test
    public void testFindProductsByQuery() {
        final String query = "Sam";

        List<Product> productsByQuery = productDao.findProductsByQuery(query);

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
        final String iphoneCode = "iphone";

        Product iPhone = productDao.getByCode(iphoneCode);

        assertEquals(iphoneCode, iPhone.getCode());
    }

    @Test
    public void testUpdateStockAfterPlaceOrder() {
        final int quantity = 5;
        final int expectedQuantity = 5;
        final Long id = 4L;
        Product productFromList = productDao.get(id);
        Order order = new Order();

        CartItem cartItem = new CartItem(productFromList, quantity);
        order.getItemList().add(cartItem);
        orderService.placeOrder(order);
        productDao.updateStock(order);

        assertEquals(expectedQuantity, productDao.get(id).getStock());
    }
}