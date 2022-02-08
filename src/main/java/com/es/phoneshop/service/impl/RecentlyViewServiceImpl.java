package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.RecentlyViewService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class RecentlyViewServiceImpl implements RecentlyViewService {

    private static final String VIEWED_SESSION_ATTRIBUTE = String.format("%s.viewedProducts", RecentlyViewServiceImpl.class.getName());
    private static final Object LOCK = new Object();

    private static volatile RecentlyViewService instance;

    private RecentlyViewServiceImpl() {
    }

    public static RecentlyViewService getInstance() {
        if (instance == null) {
            synchronized (RecentlyViewService.class) {
                if (instance == null) {
                    instance = new RecentlyViewServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void add(List<Product> recentlyViewed, Product product) {
        synchronized (LOCK) {
            if (recentlyViewed.contains(product)) {
                return;
            }
            if (recentlyViewed.size() == 3) {
                recentlyViewed.remove(2);
                recentlyViewed.add(0, product);
            } else {
                recentlyViewed.add(0, product);
            }
        }
    }

    @Override
    public List<Product> getRecentlyViewedProducts(HttpSession session) {
        synchronized (LOCK) {
            List<Product> recentlyViewedList = (List<Product>) session.getAttribute(VIEWED_SESSION_ATTRIBUTE);
            if (recentlyViewedList == null) {
                session.setAttribute(VIEWED_SESSION_ATTRIBUTE, recentlyViewedList = new ArrayList<>());
            }
            return recentlyViewedList;
        }
    }
}