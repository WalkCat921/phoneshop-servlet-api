package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.SampleProduct;
import com.es.phoneshop.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {

    private static final String IPHONE_CODE = "iphone";

    @Mock
    private HttpSession session;

    private Cart cart;
    private CartService cartService;
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        SampleProduct.createSampleProductsArrayList(productDao);
        cartService = CartServiceImpl.getInstance();
        cart = cartService.getCart(session);

    }


    @Test
    public void givenTwoSingletonsCartService_WhenAssertSame_ThenReturnTrue() {
        CartService cartServiceFirst = CartServiceImpl.getInstance();
        CartService cartServiceSecond = CartServiceImpl.getInstance();

        assertSame(cartServiceFirst, cartServiceFirst);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullCart_WhenAddInNullCart_ThenThrowNullPointerException() throws OutOfStockException {
        Cart cart = null;

        cartService.add(cart, IPHONE_CODE, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenQuantityBelowZero_WhenAddToCart_ThanThrowIllegalArgumentException() throws OutOfStockException {
        final int quantityBelowZero = -2;

        cartService.add(new Cart(), IPHONE_CODE, quantityBelowZero);
    }

    @Test(expected = OutOfStockException.class)
    public void givenQuantityHigherProductStock_WhenAddToCart_ThenThrowOutOfStockException() throws OutOfStockException {
        final int quantityHigherProductStock = 200000;

        cartService.add(new Cart(), IPHONE_CODE, quantityHigherProductStock);
    }

    @Test
    public void givenProductInfo_WhenAddProduct_ThenSumQuantity() throws OutOfStockException {
        final int quantity = 1;

        cart.getItemList().add(new CartItem(productDao.getProduct(IPHONE_CODE), 2));
        cartService.add(cart, IPHONE_CODE, quantity);

        assertEquals(3, cart.getItemList().get(0).getQuantity());

    }

    @Test
    public void givenProductInfo_WhenAddProduct_ThanReturnNotNull() throws OutOfStockException {
        final int quantity = 1;

        cartService.add(cart, IPHONE_CODE, quantity);

        assertNotNull(cart.getItemList().get(0));
    }

    @Test
    public void givenCart_WhenGetCartFromSession_ThanCartNotNull() {
        Cart cart;

        cart = cartService.getCart(session);

        assertNotNull(cart);
    }


}