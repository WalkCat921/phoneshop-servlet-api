package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SampleProduct;
import com.es.phoneshop.service.RecentlyViewService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class RecentlyViewServiceImplTest {

    private static final String IPHONE_CODE = "iphone";
    private static final String SAMSUNG_S_CODE = "sgs";
    private static final String NOKIA_3310_CODE = "nokia3310";

    @Mock
    private HttpSession session;

    private RecentlyViewService recentlyViewService;
    private ProductDao productDao;
    private List<Product> recentlyViewed;

    @Before
    public void setUp() {
        productDao = ArrayListProductDao.getInstance();
        SampleProduct.createSampleProductsArrayList(productDao);
        recentlyViewService = RecentlyViewServiceImpl.getInstance();
        recentlyViewed = recentlyViewService.getRecentlyViewedProducts(session);
    }

    @Test
    public void givenTwoSingletonRecentlyView_WhenAssertSame_ThanReturnTrue() {
        RecentlyViewService recentlyViewServiceFirst = RecentlyViewServiceImpl.getInstance();
        RecentlyViewService recentlyViewServiceSecond = RecentlyViewServiceImpl.getInstance();

        assertSame(recentlyViewServiceFirst, recentlyViewServiceSecond);
    }

    @Test
    public void givenProductsList_WhenGetRecentlyViewedProducts_ThenReturnNotNullProductsList() {
        List<Product> products;

        products = recentlyViewService.getRecentlyViewedProducts(session);

        assertNotNull(products);
    }

    @Test
    public void givenProduct_WhenAddProducts_ThenReturnTrueIfLastAdded() {
        Product nokia = productDao.getProduct(NOKIA_3310_CODE);
        fillRecentlyViewedList();

        recentlyViewService.add(recentlyViewed, nokia);

        assertEquals(nokia, recentlyViewed.get(0));
    }

    @Test
    public void givenProduct_WhenAdd_ThenListNotNull() {
        Product iphone = productDao.getProduct(IPHONE_CODE);

        recentlyViewService.add(recentlyViewed, iphone);

        assertNotNull(recentlyViewed);
    }

    @Test
    public void givenProduct_WhenAddProduct_ThenIfProductInListNoAdd() {
        Product iphone = productDao.getProduct(IPHONE_CODE);
        fillRecentlyViewedList();

        recentlyViewService.add(recentlyViewed, iphone);

        long count = recentlyViewed.stream()
                .filter(product -> product.getCode().equals(iphone.getCode()))
                .count();
        assertEquals(1, count);
    }

    private void fillRecentlyViewedList() {
        Product iphone = productDao.getProduct(IPHONE_CODE);
        Product samsung = productDao.getProduct(SAMSUNG_S_CODE);
        Product nokia = productDao.getProduct(NOKIA_3310_CODE);
        recentlyViewService.add(recentlyViewed, iphone);
        recentlyViewService.add(recentlyViewed, samsung);
        recentlyViewService.add(recentlyViewed, nokia);
    }
}