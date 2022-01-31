package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.price.PriceHistory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PopupWindowServlet extends HttpServlet {

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
        request.setAttribute("priceHistory", priceHistory);
        request.setAttribute("product", productDao.getProduct(code));
        request.getRequestDispatcher("/WEB-INF/pages/priceHistoryPopup.jsp").forward(request, response);
    }

}
