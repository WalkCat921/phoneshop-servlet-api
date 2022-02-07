package com.es.phoneshop.web;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.model.price.PriceHistory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PopupWindowServlet extends HttpServlet {

    private static final String PRICE_HISTORY_ATTRIBUTE = "priceHistory";
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String JSP_PATH = "/WEB-INF/pages/priceHistoryPopup.jsp";

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getPathInfo().substring(1);
        PriceHistory priceHistory = productDao.getProduct(code).getPriceHistory();
        request.setAttribute(PRICE_HISTORY_ATTRIBUTE, priceHistory);
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(code));
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

}
