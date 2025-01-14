package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.RecentlyViewService;
import com.es.phoneshop.service.impl.CartServiceImpl;
import com.es.phoneshop.service.impl.RecentlyViewServiceImpl;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ProductListPageServlet extends HttpServlet {

    private static final String ERROR_SESSION_ATTRIBUTE = "ErrorSessionProductListPage";
    private static final String QUANTITY_SESSION_ATTRIBUTE = "QuantitySessionProductListPage";
    private static final String QUERY_PARAM = "query";
    private static final String SORT_FIELD_PARAM = "sort";
    private static final String SORT_ORDER_PARAM = "order";
    private static final String RECENTLY_VIEW_ATTRIBUTE = "recentlyView";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String PRODUCT_CODE_PARAM = "productCode";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String CACHE_HEADER = "Cache-Control";
    private static final String CACHE_HEADER_PARAMETERS = "no-cache, no-store, must-revalidate";
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
        setAttributeFromSession(request, ERROR_SESSION_ATTRIBUTE, ERROR_ATTRIBUTE);
        setAttributeFromSession(request, QUANTITY_SESSION_ATTRIBUTE, QUANTITY_PARAM);
        request.setAttribute(RECENTLY_VIEW_ATTRIBUTE,
                recentlyViewService.getRecentlyViewedProducts(request.getSession()));
        response.setHeader(CACHE_HEADER, CACHE_HEADER_PARAMETERS);
        request.getRequestDispatcher(PRODUCT_LIST_JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter(PRODUCT_CODE_PARAM);
        String quantityString = request.getParameter(QUANTITY_PARAM);
        String query = request.getParameter(QUERY_PARAM);
        Cart cart = cartService.getCart(request.getSession());
        Map<String, String> errors = new HashMap<>();
        Map<String, String> quantities = new HashMap<>();
        try {
            quantities.put(code, quantityString);
            cartService.add(cart, code, parseQuantity(quantityString, request));
            if (query.isEmpty()) {
                response.sendRedirect(String.format("%s/products?message=Added to cart successfully",
                        request.getContextPath()));
            } else {
                response.sendRedirect(String.format("%s/products?message=Added to cart successfully&query=%s",
                        request.getContextPath(),
                        query));
            }
            request.getSession().removeAttribute(ERROR_SESSION_ATTRIBUTE);
            request.getSession().removeAttribute(QUANTITY_SESSION_ATTRIBUTE);
        } catch (Exception e) {
            errors.put(code, getMessageException(e));
            setValueInSession(request, ERROR_SESSION_ATTRIBUTE, errors);
            setValueInSession(request, QUANTITY_SESSION_ATTRIBUTE, quantities);
            response.sendRedirect(String.format("%s/products", request.getContextPath()));
        }
    }

    private int parseQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        if (NumberUtils.isDigits(quantityString)) {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            return format.parse(quantityString).intValue();
        } else {
            throw new IllegalArgumentException("Not a number");
        }
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

    private void setAttributeFromSession(HttpServletRequest request, String sessionAttribute,
                                         String attribute) {
        if (request.getSession().getAttribute(sessionAttribute) != null) {
            request.setAttribute(attribute, request.getSession().getAttribute(sessionAttribute));
        }
    }

    private void setValueInSession(HttpServletRequest request, String sessionAttribute, Object object) {
        request.getSession().setAttribute(sessionAttribute, object);
    }
}