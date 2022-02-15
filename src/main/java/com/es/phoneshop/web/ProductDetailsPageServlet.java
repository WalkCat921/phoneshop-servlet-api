package com.es.phoneshop.web;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.RecentlyViewService;
import com.es.phoneshop.service.impl.CartServiceImpl;
import com.es.phoneshop.service.impl.RecentlyViewServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

public class ProductDetailsPageServlet extends HttpServlet {

    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String RECENTLY_VIEW_ATTRIBUTE = "recentlyView";
    private static final String PRODUCT_DETAILS_JSP_PATH = "/WEB-INF/pages/productDetails.jsp";

    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewService recentlyViewService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImpl.getInstance();
        recentlyViewService = RecentlyViewServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productCode = parseProductCode(request);
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(productCode));
        request.setAttribute(CART_ATTRIBUTE, cartService.getCart(request.getSession()));
        List<Product> recentlyViewedList = recentlyViewService.getRecentlyViewedProducts(request.getSession());
        recentlyViewService.add(recentlyViewedList, productDao.getProduct(productCode));
        request.setAttribute(RECENTLY_VIEW_ATTRIBUTE,
                recentlyViewService.getRecentlyViewedProducts(request.getSession()));
        request.getRequestDispatcher(PRODUCT_DETAILS_JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter(QUANTITY_PARAM);
        String productCode = parseProductCode(request);
        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
            Cart cart = cartService.getCart(request.getSession());
            cartService.add(cart, productCode, quantity);
            response.sendRedirect(String.format("%s/products/%s?message=Product added to cart successfully",
                    request.getContextPath(),
                    productCode));
        } catch (Exception e) {
            response.sendRedirect(String.format("%s/products/%s?error=%s",
                    request.getContextPath(),
                    productCode,
                    getMessageException(e)));

        }
    }

    private String parseProductCode(HttpServletRequest request) {
        return request.getPathInfo().substring(1);
    }

    private String getMessageException(Exception e) {
        if (e.getClass() == ParseException.class) {
            return "Not a number!";
        } else if (e.getClass() == OutOfStockException.class) {
            OutOfStockException ofStockException = (OutOfStockException) e;
            return ofStockException.getMessage();
        } else {
            return e.getMessage();
        }
    }
}