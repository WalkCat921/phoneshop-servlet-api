package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private CartService cartService;
    @Mock
    private ServletConfig config;
    @Mock
    private Cart cart;

    @InjectMocks
    private final DeleteCartItemServlet servlet = new DeleteCartItemServlet();

    private static final String MOCKED_PATH_INFO = "iphone";

    @Before
    public void setup() throws ServletException {
        when(request.getPathInfo()).thenReturn(MOCKED_PATH_INFO);
        when(request.getSession()).thenReturn(session);
        when(cartService.getCart(any())).thenReturn(cart);
    }

    @Test
    public void testDoPost() throws IOException, ServletException {
        servlet.init(config);

        servlet.doPost(request, response);

        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    public void testInvokedDelete() throws ServletException, IOException {
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getPathInfo()).thenReturn("/sgs");

        servlet.doPost(request, response);

        verify(cartService, atLeast(1)).delete(any(), anyString());
    }
}