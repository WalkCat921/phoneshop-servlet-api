package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SampleProduct;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SampleDataServletContextListener implements ServletContextListener {

    private ProductDao productDao;

    public SampleDataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (Boolean.valueOf(event.getServletContext().getInitParameter("createSampleData"))) {
            SampleProduct.createSampleProductsArrayList(productDao);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}