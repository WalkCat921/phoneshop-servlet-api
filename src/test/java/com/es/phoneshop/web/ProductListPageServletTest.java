package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.RecentlyViewService;
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
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private ProductDao productDao;
    @Mock
    private ServletConfig config;
    @Mock
    private RecentlyViewService recentlyViewService;

    private static final String QUERY_PARAM = "query";
    private static final String SORT_FIELD_PARAM = "sort";
    private static final String SORT_ORDER_PARAM = "order";
    private static final String PRODUCT_CODE_PARAM = "productCode";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String PRODUCT_LIST_JSP_PATH = "/WEB-INF/pages/productList.jsp";


    @InjectMocks
    private ProductListPageServlet servlet = new ProductListPageServlet();

    @Before
    public void setup() throws ServletException {
        when(request.getSession()).thenReturn(session);
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).getRequestDispatcher(eq(PRODUCT_LIST_JSP_PATH));
        verify(request).setAttribute(eq(PRODUCTS_ATTRIBUTE), any());
        verify(request.getRequestDispatcher(anyString()), atLeast(1)).forward(request, response);
    }

    @Test
    public void testDоPostCatchException() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(response, atLeast(1)).sendRedirect(anyString());
    }


    @Test
    public void testDоPostInvokedProductDaoMethod() throws ServletException, IOException {
        when(request.getParameter(SORT_FIELD_PARAM)).thenReturn(String.valueOf(SortField.DESCRIPTION));
        when(request.getParameter(SORT_ORDER_PARAM)).thenReturn(String.valueOf(SortOrder.ASC));

        servlet.doGet(request, response);

        verify(productDao, times(1)).findProductsByQuerySortFieldAndOrder(null, SortField.DESCRIPTION, SortOrder.ASC);
        verify(request, times(1)).getRequestDispatcher(eq(PRODUCT_LIST_JSP_PATH));
    }


    @Test
    public void testDоPostSendRedirectIfQueryNotEmpty() throws ServletException, IOException, OutOfStockException {
        final int quantity = 2;
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(request.getParameter(PRODUCT_CODE_PARAM)).thenReturn("");
        when(request.getParameter(QUANTITY_PARAM)).thenReturn("2");
        when(request.getParameter(QUERY_PARAM)).thenReturn("iphone");

        servlet.doPost(request, response);

        verify(cartService, atLeast(1)).add(cart, "", quantity);
        verify(response, atLeast(1)).sendRedirect(anyString());
    }

    @Test
    public void testDоPostSendRedirectIfQueryEmpty() throws ServletException, IOException, OutOfStockException {
        final int quantity = 2;
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(request.getParameter(PRODUCT_CODE_PARAM)).thenReturn("");
        when(request.getParameter(QUANTITY_PARAM)).thenReturn("2");
        when(request.getParameter(QUERY_PARAM)).thenReturn("");

        servlet.doPost(request, response);

        verify(cartService, atLeast(1)).add(cart, "", quantity);
        verify(response, atLeast(1)).sendRedirect(anyString());
    }

    @Test
    public void testServletInvokedInitMethod() throws ServletException {
        servlet.init(config);
    }
}