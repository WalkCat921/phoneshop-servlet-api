package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.CartServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {

    private static final String ORDER_ATTRIBUTE = "order";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String ORDER_OVERVIEW_JSP_PATH = "/WEB-INF/pages/orderOverview.jsp";

    private OrderDao orderDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureOrderId = request.getPathInfo().substring(1);
        Cart cart = cartService.getCart(request.getSession());
        request.setAttribute(CART_ATTRIBUTE, cart);
        request.setAttribute(ORDER_ATTRIBUTE, orderDao.getBySecureId(secureOrderId));
        request.getRequestDispatcher(ORDER_OVERVIEW_JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}