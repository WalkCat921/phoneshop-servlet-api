package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    @Mock
    private ServletConfig config;
    @Mock
    private Cart cart;
    @Mock
    private Order order;
    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;
    @Mock
    private ProductDao productDao;

    @Mock
    private List<CartItem> list;

    @InjectMocks
    CheckoutPageServlet servlet = new CheckoutPageServlet();

    private static final String FIRST_NAME_PARAM = "firstName";
    private static final String LAST_NAME_PARAM = "lastName";
    private static final String PHONE_PARAM = "phone";
    private static final String DELIVERY_DATE_PARAM = "deliveryDate";
    private static final String DELIVERY_ADDRESS_PARAM = "deliveryAddress";
    private static final String CACHE_HEADER = "Cache-Control";
    private static final String CACHE_HEADER_PARAMETERS = "no-cache, no-store, must-revalidate";
    private static final String PAYMENT_METHOD_PARAM = "paymentMethod";
    private static final String CONTEXT_PATH_PRODUCTS = "products";
    private static final String CONTEXT_PATH_CHECKOUT = "/checkout";

    @Before
    public void setUp() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(cartService.getCart(any())).thenReturn(cart);
        when(orderService.getOrder(any())).thenReturn(order);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testInitMethod() throws ServletException {
        servlet.init(config);
    }

    @Test
    public void testDoPostWithNotEmptyErrors() throws ServletException, IOException {
        when(request.getContextPath()).thenReturn(StringUtils.EMPTY);

        servlet.doPost(request, response);

        verify(response, times(1)).sendRedirect(eq(CONTEXT_PATH_CHECKOUT));
    }

    @Test
    public void testDoPostWithoutErrors() throws IOException, ServletException {
        final String firstName = "Test";
        final String lastName = "Test";
        final String phone = "3750123456789";
        final String deliveryData = LocalDate.now().toString();
        final String deliveryAddress = "st. Test 1-78, Test";
        final String paymentMethod = "CASH";
        when(request.getParameter(FIRST_NAME_PARAM)).thenReturn(firstName);
        when(request.getParameter(LAST_NAME_PARAM)).thenReturn(lastName);
        when(request.getParameter(PHONE_PARAM)).thenReturn(phone);
        when(request.getParameter(DELIVERY_DATE_PARAM)).thenReturn(deliveryData);
        when(request.getParameter(DELIVERY_ADDRESS_PARAM)).thenReturn(deliveryAddress);
        when(request.getParameter(PAYMENT_METHOD_PARAM)).thenReturn(paymentMethod);

        servlet.doPost(request, response);

        verify(orderService, times(1)).placeOrder(any());
        verify(cartService, times(1)).clear(any());
        verify(response, times(1)).sendRedirect(anyString());
        verify(productDao, times(1)).updateStock(any());
    }

    @Test
    public void testDoGetResponseSetHeader() throws ServletException, IOException {
        when(cart.getItemList()).thenReturn(list);

        servlet.doGet(request, response);

        verify(response, times(1)).setHeader(eq(CACHE_HEADER), eq(CACHE_HEADER_PARAMETERS));
    }

    @Test
    public void testDoGetSendRedirectIfCartItemListNull() throws ServletException, IOException {
        when(request.getContextPath()).thenReturn(CONTEXT_PATH_PRODUCTS);

        servlet.doGet(request, response);

        verify(response, times(1)).sendRedirect(eq(CONTEXT_PATH_PRODUCTS));
    }
}