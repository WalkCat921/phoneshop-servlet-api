package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private HttpSession session;
    @Mock
    private Cart cart;
    @Mock
    private CartService cartService;
    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();

    private static final String SECURITY_CODE = "/e59ba1f5-f3cc-48ca-90e0-32500b076d74";
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String ORDER_OVERVIEW_JSP_PATH = "/WEB-INF/pages/orderOverview.jsp";

    @Before
    public void setUp() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getPathInfo()).thenReturn(SECURITY_CODE);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testInitMethodInvoked() throws ServletException {
        servlet.init(servletConfig);
    }

    @Test
    public void testDoPostInvokedDoGet() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(request).getRequestDispatcher(eq(ORDER_OVERVIEW_JSP_PATH));
        verify(request.getRequestDispatcher(ORDER_OVERVIEW_JSP_PATH), times(1)).forward(request, response);
    }

    @Test
    public void testDoGetWorkCorrectly() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(cartService, times(1)).getCart(any());
        verify(request).setAttribute(eq(CART_ATTRIBUTE), any());
        verify(request).setAttribute(eq(ORDER_ATTRIBUTE), any());
        verify(orderDao, times(1)).getBySecureId(anyString());
        verify(request).getRequestDispatcher(eq(ORDER_OVERVIEW_JSP_PATH));
    }
}