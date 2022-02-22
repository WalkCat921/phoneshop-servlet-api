package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
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
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {

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

    private static final String ERROR_ATTRIBUTE = "errors";
    private static final String CART_JSP_PATH = "/WEB-INF/pages/cart.jsp";

    @InjectMocks
    private CartPageServlet servlet = new CartPageServlet();

    @Before
    public void setup() throws ServletException, OutOfStockException {
        when(request.getSession()).thenReturn(session);
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenRequestResponse_WhenDoGet_ThenGetRequestDispatcher() throws ServletException, IOException {
        servlet.init(servletConfig);

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher(CART_JSP_PATH);
    }

    @Test
    public void givenRequestResponse_WhenDoGet_ThenInvokedSetAttributeOneTime() throws ServletException, IOException {
        servlet.init(servletConfig);

        servlet.doGet(request, response);

        verify(request, times(1)).setAttribute(anyString(), any());
    }

    @Test
    public void testDoPostRequestInvokedSetAttributeError() throws ServletException, IOException, OutOfStockException {
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(request.getParameterValues("productCode")).thenReturn(new String[]{"sgs", "iphone", "sgs3"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"sdf", "2", "3"});

        servlet.doPost(request, response);

        verify(request, atLeast(1)).setAttribute(eq(ERROR_ATTRIBUTE), any());
    }

    @Test
    public void testDoPostResponseInvokedSendRedirect() throws ServletException, IOException, OutOfStockException {
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(request.getParameterValues("productCode")).thenReturn(new String[]{"sgs", "iphone", "sgs3"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1", "2", "3"});

        servlet.doPost(request, response);

        verify(response, atLeast(1)).sendRedirect(any());
    }

    @Test
    public void testDoPostsResponseInvokedSendRedirect() throws ServletException, IOException, OutOfStockException {
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(request.getParameterValues("productCode")).thenReturn(new String[]{"sgs", "iphone", "sgs3"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"100000", "200000", "100000"});

        servlet.doPost(request, response);

        verify(response, atLeast(1)).sendRedirect(any());
    }
}