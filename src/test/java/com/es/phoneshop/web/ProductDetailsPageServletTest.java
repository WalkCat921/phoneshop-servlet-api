package com.es.phoneshop.web;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.model.product.SampleProduct;
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
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {

    private static final String MOCKED_PATH_INFO = "/iphone";

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

    private ProductDao productDao = ArrayListProductDao.getInstance();

    @InjectMocks
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        SampleProduct.createSampleProductsArrayList(productDao);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn(MOCKED_PATH_INFO);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
    }

    @Test
    public void testSetProductAttribute() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("product"), any());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetProductWithWrongCodeInPath() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("-231");

        servlet.doGet(request, response);
    }

    @Test(expected = NullPointerException.class)
    public void testTryGetProductWithEmptyPathInfo() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/");

        servlet.doGet(request, response);
    }

    @Test
    public void testGetRequestDispatcher() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/productDetails.jsp"));
    }

    @Test
    public void testRequestInvokedGetPathInfoMethod() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request, atLeast(1)).getPathInfo();
    }

    @Test
    public void givenRequestResponse_WhenDoPost_ThenInvokedSendRedirectOneTime() throws ServletException, IOException {

        servlet.doPost(request, response);

        verify(response, atLeast(1)).sendRedirect(anyString());
    }

    @Test
    public void givenRequestResponse_WhenDoGet_ThenInvokedGetSessionMethodAtLeastOneTime() throws ServletException, IOException {

        servlet.doGet(request, response);

        verify(request, atLeast(1)).getSession();
    }
}