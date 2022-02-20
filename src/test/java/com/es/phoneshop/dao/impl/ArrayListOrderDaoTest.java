package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.order.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListOrderDaoTest {

    @Mock
    private Order firstOrder;
    @Mock
    private Order secondOrder;
    @Mock
    private Order thirdOrder;
    @Spy
    private ArrayList<Order> orders;

    @InjectMocks
    private final OrderDao orderDao = ArrayListOrderDao.getInstance();

    private static final String FIRST_SECURE_CODE = "test-id-1";
    private static final String SECOND_SECURE_CODE = "test-id-2";
    private static final String THIRD_SECURE_CODE = "test-id-3";

    @Before
    public void setUp() throws Exception {
        when(firstOrder.getSecureId()).thenReturn(FIRST_SECURE_CODE);
        when(secondOrder.getSecureId()).thenReturn(SECOND_SECURE_CODE);
        when(thirdOrder.getSecureId()).thenReturn(THIRD_SECURE_CODE);
        orders.addAll(List.of(firstOrder, secondOrder, thirdOrder));
    }

    @After
    public void end(){
        orders.clear();
    }

    @Test
    public void testGetOrderBySecureIdEqualsFirstOrder(){
        assertEquals(firstOrder,orderDao.getBySecureId(FIRST_SECURE_CODE));
        assertEquals(secondOrder,orderDao.getBySecureId(SECOND_SECURE_CODE));
        assertEquals(thirdOrder,orderDao.getBySecureId(THIRD_SECURE_CODE));
    }

    @Test(expected = NoSuchElementException.class)
    public void test(){
        final String wrongSecureId = "wrongId";

        orderDao.getBySecureId(wrongSecureId);
    }
}