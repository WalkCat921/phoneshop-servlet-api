package com.es.phoneshop.web;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;
import com.es.phoneshop.service.RecentlyViewService;
import com.es.phoneshop.service.impl.RecentlyViewServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {

    private static final String QUERY_PARAM = "query";
    private static final String SORT_FIELD_PARAM = "sort";
    private static final String SORT_ORDER_PARAM = "order";
    private static final String RECENTLY_VIEW_ATTRIBUTE = "recentlyView";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String JSP_PATH = "/WEB-INF/pages/productList.jsp";

    private ProductDao productDao;
    private RecentlyViewService recentlyViewService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
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
        request.setAttribute(RECENTLY_VIEW_ATTRIBUTE, recentlyViewService.getRecentlyViewedProducts(request.getSession()));
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

}
