package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SampleDataServletContextListenerTest {

    @InjectMocks
    private final SampleDataServletContextListener servlet = new SampleDataServletContextListener();

    @Mock
    private ServletContext servletContext;
    @Mock
    private ServletContextEvent event;
    @Mock
    private ProductDao productDao;

    @Before
    public void setup() {
        when(event.getServletContext()).thenReturn(servletContext);
    }

    @Test
    public void testCreateSampleData() {
        when(servletContext.getInitParameter("createSampleData")).thenReturn("true");

        servlet.contextInitialized(event);

        verify(productDao, atLeast(1)).save(any());
    }

    @Test
    public void testNoCreateSampleData() {
        when(servletContext.getInitParameter("createSampleData")).thenReturn("false");

        servlet.contextInitialized(event);

        verify(productDao, never()).save(any());
    }
}