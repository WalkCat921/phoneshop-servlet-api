package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private Cart cart;

    @InjectMocks
    private final OrderService orderService = OrderServiceImpl.getInstance();

    @Test
    public void testOrderServiceSingleton() {
        OrderService first;
        OrderService second;

        first = OrderServiceImpl.getInstance();
        second = OrderServiceImpl.getInstance();

        assertSame(first, second);
    }

    @Test
    public void testGetOrder() {
        when(cart.getTotalCost()).thenReturn(BigDecimal.TEN);
        when(cart.getItemList()).thenReturn(new ArrayList<CartItem>());

        Order order = orderService.getOrder(cart);

        assertEquals(cart.getTotalCost(), order.getSubtotal());
    }

    @Test
    public void testDeepCopyOfCartItemList() {
        when(cart.getTotalCost()).thenReturn(BigDecimal.TEN);
        when(cart.getItemList()).thenReturn(new ArrayList<>());

        Order order = orderService.getOrder(cart);

        assertFalse(cart.getItemList() == order.getItemList());
    }

    @Test
    public void testGetPaymentMethods() {
        assertEquals(List.of(PaymentMethod.values()), orderService.getPaymentMethods());
    }

    @Test
    public void testPlaceOrder() {
        final int timeOfInvocations = 1;
        Order order = new Order();

        orderService.placeOrder(order);

        assertNotNull(order.getSecureId());
        verify(orderDao, times(timeOfInvocations)).save(order);
    }
}