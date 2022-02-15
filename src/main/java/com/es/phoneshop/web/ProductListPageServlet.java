package com.es.phoneshop.web;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;
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

public class ProductListPageServlet extends HttpServlet {

    private static final String QUERY_PARAM = "query";
    private static final String SORT_FIELD_PARAM = "sort";
    private static final String SORT_ORDER_PARAM = "order";
    private static final String RECENTLY_VIEW_ATTRIBUTE = "recentlyView";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String PRODUCT_CODE_PARAM = "productCode";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String PRODUCT_LIST_JSP_PATH = "/WEB-INF/pages/productList.jsp";

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
        String query = request.getParameter(QUERY_PARAM);
        String sortField = request.getParameter(SORT_FIELD_PARAM);
        String sortOrder = request.getParameter(SORT_ORDER_PARAM);
        if (sortField == null && sortOrder == null) {
            request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.findProductsByQuery(query));
        } else {
            request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.findProductsByQuerySortFieldAndOrder(query,
                    SortField.getSortFieldByRequestParam(sortField),
                    SortOrder.getSortOrderByRequestParam(sortOrder)));
        }
        request.setAttribute(RECENTLY_VIEW_ATTRIBUTE,
                recentlyViewService.getRecentlyViewedProducts(request.getSession()));
        request.getRequestDispatcher(PRODUCT_LIST_JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter(PRODUCT_CODE_PARAM);
        String quantityString = request.getParameter(QUANTITY_PARAM);
        String query = request.getParameter(QUERY_PARAM);
        Cart cart = cartService.getCart(request.getSession());
        try {
            cartService.add(cart, code, parseQuantity(quantityString, request));
            if (query.isEmpty()) {
                response.sendRedirect(String.format("%s/products?message=Added to cart successfully",
                        request.getContextPath()));
            } else {
                response.sendRedirect(String.format("%s/products?message=Added to cart successfully&query=%s",
                        request.getContextPath(),
                        query));
            }
        } catch (Exception e) {
            request.setAttribute(ERROR_ATTRIBUTE, getMessageException(e));
            doGet(request, response);
            return;
        }
    }

    private int parseQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityString).intValue();
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
