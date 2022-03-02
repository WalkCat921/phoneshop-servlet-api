package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.search.SearchFilter;
import com.es.phoneshop.validator.FormValidator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPageServlet extends HttpServlet {

    private static final String ERROR_ATTRIBUTE = "errors";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String PRODUCT_CODE_PARAM = "productCode";
    private static final String MIN_PRICE_PARAM = "minPrice";
    private static final String MAX_PRICE_PARAM = "maxPrice";
    private static final String MIN_STOCK_PARAM = "minStock";
    private static final String SEARCH_JSP_PATH = "/WEB-INF/pages/search.jsp";

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        List<Product> filteredProducts = productDao.findAll();
        if (request.getParameterMap().isEmpty()) {
            request.setAttribute(PRODUCTS_ATTRIBUTE, filteredProducts);
            request.getRequestDispatcher(SEARCH_JSP_PATH).forward(request, response);
            return;
        }
        String productCodeString = request.getParameter(PRODUCT_CODE_PARAM);
        String minPriceString = request.getParameter(MIN_PRICE_PARAM);
        String maxPriceString = request.getParameter(MAX_PRICE_PARAM);
        String minStockString = request.getParameter(MIN_STOCK_PARAM);
        FormValidator.validateSearchForm(minPriceString, MIN_PRICE_PARAM, errors);
        FormValidator.validateSearchForm(maxPriceString, MAX_PRICE_PARAM, errors);
        FormValidator.validateSearchForm(minStockString, MIN_STOCK_PARAM, errors);
        if (errors.isEmpty()) {
            filteredProducts = SearchFilter.getListBySearchFilters(productCodeString, parseStock(minStockString),
                    parsePrice(minPriceString), parsePrice(maxPriceString));
            request.setAttribute(PRODUCTS_ATTRIBUTE, filteredProducts);
            request.getRequestDispatcher(SEARCH_JSP_PATH).forward(request, response);
            return;
        } else {
            request.setAttribute(PRODUCTS_ATTRIBUTE, filteredProducts);
            request.setAttribute(ERROR_ATTRIBUTE, errors);
            request.getRequestDispatcher(SEARCH_JSP_PATH).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private BigDecimal parsePrice(String priceString) {
        if (priceString == null || priceString.isEmpty()) {
            return null;
        }
        return new BigDecimal(priceString);
    }

    private int parseStock(String stockString) {
        if (stockString == null || stockString.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(stockString);

        }
    }

}