package com.es.phoneshop.web;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.model.product.SampleProduct;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PopupWindowServletTest {

    private final String mockedPathInfo = "/iphone";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;

    private PopupWindowServlet servlet = new PopupWindowServlet();

    private ProductDao productDao = ArrayListProductDao.getInstance();

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        SampleProduct.createSampleProductsArrayList(productDao);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn(mockedPathInfo);

    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
    }


    @Test
    public void testRequestSetAttributePriceHistory() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("priceHistory"), any());
    }

    @Test
    public void testSetProductAttribute() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("product"), any());
    }

    @Test
    public void testGetRequestDispatcher() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/priceHistoryPopup.jsp"));
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
}