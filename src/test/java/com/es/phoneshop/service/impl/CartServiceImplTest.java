package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
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
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {

    private static final String IPHONE_CODE = "iphone";
    private static final String NOKIA_CODE = "nokia3310";
    private static final String SAMSUNG_CODE = "sgs";

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

        assertSame(cartServiceFirst, cartServiceSecond);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullCart_WhenAddInNullCart_ThenThrowNullPointerException() throws OutOfStockException {
        Cart cart = null;
        final int quantity = 1;

        cartService.add(cart, IPHONE_CODE, quantity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenQuantityBelowZero_WhenAddToCart_ThenThrowIllegalArgumentException() throws OutOfStockException {
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
        final int firstQuantity = 2;
        final int secondQuantity = 1;
        final int index = 0;
        final int expectedQuantity = 3;

        cart.getItemList().add(new CartItem(productDao.getByCode(IPHONE_CODE), firstQuantity));
        cartService.add(cart, IPHONE_CODE, secondQuantity);

        assertEquals(expectedQuantity, cart.getItemList().get(index).getQuantity());

    }

    @Test
    public void givenProductInfo_WhenAddProduct_ThenReturnNotNull() throws OutOfStockException {
        final int quantity = 1;
        final int index = 0;

        cartService.add(cart, IPHONE_CODE, quantity);

        assertNotNull(cart.getItemList().get(index));
    }

    @Test
    public void givenCart_WhenGetCartFromSession_ThenCartNotNull() {
        Cart cart;

        cart = cartService.getCart(session);

        assertNotNull(cart);
    }

    @Test
    public void givenCart_WhenUpdate_ThenQuantityChanged() throws OutOfStockException {
        Cart cart = cartService.getCart(session);
        final int quantityBeforeUpdate = 1;
        final int quantity = 4;
        cartService.add(cart, IPHONE_CODE, quantityBeforeUpdate);

        cartService.update(cart, IPHONE_CODE, quantity);

        assertNotEquals(quantityBeforeUpdate, cart.getItemList().get(0).getQuantity());
    }

    @Test(expected = NullPointerException.class)
    public void givenWrongCart_WhenUpdate_ThenThrowNullPointerException() throws OutOfStockException {
        Cart wrongCart = null;
        final int quantity = 1;

        cartService.add(wrongCart, IPHONE_CODE, quantity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenWrongQuantity_WhenUpdate_ThenThrowIllegalArgumentException() throws OutOfStockException {
        final int wrongQuantity = -123;

        cartService.add(new Cart(), IPHONE_CODE, wrongQuantity);
    }

    @Test(expected = OutOfStockException.class)
    public void givenQuantityMoreThenStock_WhenUpdate_ThenThrowOutOfStockException() throws OutOfStockException {
        final int quantityMoreStock = 1_000_000;

        cartService.add(new Cart(), IPHONE_CODE, quantityMoreStock);
    }

    @Test(expected = NoSuchElementException.class)
    public void givenWrongProductCode_WhenUpdate_ThenThrowNoSuchElementException() throws OutOfStockException {
        final String wrongProductCode = "wrong_test";
        final int quantity = 1;

        cartService.add(new Cart(), wrongProductCode, quantity);
    }

    @Test
    public void givenCart_WhenDelete_ThenCartIsEmpty() throws OutOfStockException {
        final int quantity = 4;
        Cart cart = cartService.getCart(session);
        cartService.add(cart, IPHONE_CODE, quantity);

        cartService.delete(cart, IPHONE_CODE);

        assertTrue(cart.getItemList().isEmpty());
    }

    @Test
    public void givenCart_WhenClear_ThenCartItemListIsEmpty() throws OutOfStockException {
        final int quantity = 5;
        Cart cart = cartService.getCart(session);
        cartService.add(cart, IPHONE_CODE, quantity);
        cartService.add(cart, NOKIA_CODE, quantity);
        cartService.add(cart, SAMSUNG_CODE, quantity);

        cartService.clear(cart);

        assertTrue(cart.getItemList().isEmpty());
    }
}