package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.SampleProduct;
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
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {

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
    private CartService cartService;
    @Mock
    private Cart cart;

    private static final String MOCKED_PATH_INFO = "/iphone";
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String PRODUCT_DETAILS_JSP_PATH = "/WEB-INF/pages/productDetails.jsp";

    private ProductDao productDao = ArrayListProductDao.getInstance();

    @InjectMocks
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() throws ServletException {
        SampleProduct.createSampleProductsArrayList(productDao);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn(MOCKED_PATH_INFO);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.init(servletConfig);

        servlet.doGet(request, response);
    }

    @Test
    public void testSetProductAttribute() throws ServletException, IOException {
        servlet.init(servletConfig);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq(PRODUCT_ATTRIBUTE), any());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetProductWithWrongCodeInPath() throws ServletException, IOException {
        servlet.init(servletConfig);
        when(request.getPathInfo()).thenReturn("-231");

        servlet.doGet(request, response);
    }

    @Test(expected = NoSuchElementException.class)
    public void testTryGetProductWithEmptyPathInfo() throws ServletException, IOException {
        servlet.init(servletConfig);
        when(request.getPathInfo()).thenReturn("/");

        servlet.doGet(request, response);
    }

    @Test
    public void testGetRequestDispatcher() throws ServletException, IOException {
        servlet.init(servletConfig);

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher(eq(PRODUCT_DETAILS_JSP_PATH));
    }

    @Test
    public void testRequestInvokedGetPathInfoMethod() throws ServletException, IOException {
        servlet.init(servletConfig);

        servlet.doGet(request, response);

        verify(request, atLeast(1)).getPathInfo();
    }

    @Test
    public void givenRequestResponse_WhenDoPost_ThenInvokedSendRedirectOneTime() throws ServletException, IOException {
        servlet.init(servletConfig);

        servlet.doPost(request, response);

        verify(response, atLeast(1)).sendRedirect(anyString());
    }

    @Test
    public void givenRequestResponse_WhenDoGet_ThenInvokedGetSessionMethodAtLeastOneTime() throws ServletException, IOException {
        servlet.init(servletConfig);

        servlet.doGet(request, response);

        verify(request, atLeast(1)).getSession();
    }

    @Test
    public void testDoPostCartServiceInvokedAdd() throws OutOfStockException, IOException, ServletException {
        final int quantity = 2;
        when(cartService.getCart(any())).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(request.getParameter(QUANTITY_PARAM)).thenReturn("2");

        servlet.doPost(request, response);

        verify(cartService, atLeast(1)).add(cart, MOCKED_PATH_INFO.substring(1), quantity);
        verify(response, atLeast(1)).sendRedirect(anyString());
    }
}