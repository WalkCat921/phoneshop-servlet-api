package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.CartServiceImpl;
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

public class CartPageServlet extends HttpServlet {
    private static final String ERROR_SESSION_ATTRIBUTE = "errorsMapCartPage";
    private static final String QUANTITY_SESSION_ATTRIBUTE = "quantityMapCartPage";
    private static final String PRODUCT_CODE_PARAM = "productCode";
    private static final String PRODUCT_QUANTITY_ATTRIBUTE = "quantity";
    private static final String ERROR_ATTRIBUTE = "errors";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String CART_JSP_PATH = "/WEB-INF/pages/cart.jsp";

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(CART_ATTRIBUTE, cartService.getCart(request.getSession()));
        setAttributeFromSession(request, ERROR_SESSION_ATTRIBUTE, ERROR_ATTRIBUTE);
        setAttributeFromSession(request, QUANTITY_SESSION_ATTRIBUTE, PRODUCT_QUANTITY_ATTRIBUTE);
        request.getRequestDispatcher(CART_JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productCodes = request.getParameterValues(PRODUCT_CODE_PARAM);
        String[] quantities = request.getParameterValues(PRODUCT_QUANTITY_ATTRIBUTE);
        Map<String, String> errors = new HashMap<>();
        Map<String, Integer> productQuantity = new HashMap<>();
        for (int i = 0; i < productCodes.length; i++) {
            String code = productCodes[i];
            int quantity = 0;
            try {
                quantity = parseQuantity(quantities[i], request);
                cartService.update(cartService.getCart(request.getSession()), code, quantity);

            } catch (Exception e) {
                getMessageException(errors, code, e);
                productQuantity.put(code, quantity);
                setValueInSession(request, ERROR_SESSION_ATTRIBUTE, errors);
                setValueInSession(request, QUANTITY_SESSION_ATTRIBUTE, productQuantity);
            }
        }
        if (errors.isEmpty()) {
            request.getSession().removeAttribute(ERROR_SESSION_ATTRIBUTE);
            request.getSession().removeAttribute(QUANTITY_SESSION_ATTRIBUTE);
            response.sendRedirect(String.format("%s/cart?message=Cart updated successfully", request.getContextPath()));
        } else {
            response.sendRedirect(String.format("%s/cart", request.getContextPath()));
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

    private void getMessageException(Map<String, String> errors, String productCode, Exception e) {
        if (e.getClass() == ParseException.class) {
            errors.put(productCode, "Not a number!");
        } else if (e.getClass() == OutOfStockException.class) {
            OutOfStockException outOfStockException = (OutOfStockException) e;
            errors.put(productCode, outOfStockException.getMessage());
        } else {
            errors.put(productCode, e.getMessage());
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