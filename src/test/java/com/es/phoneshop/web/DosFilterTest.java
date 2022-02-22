package com.es.phoneshop.web;

import com.es.phoneshop.security.DosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private DosProtectionService dosProtectionService;
    @Mock
    private FilterConfig config;

    private Boolean bool = true;

    @InjectMocks
    private DosFilter filter;

    private static final int TOO_MANY_REQUESTS_CODE = 429;
    private static final String IP = "127.0.0.1";

    @Before
    public void setUp() throws Exception {
        when(dosProtectionService.isAllowed(anyString())).thenReturn(true);
    }

    @Test
    public void testInitMethodInvoked() throws ServletException {
        filter.init(config);
    }

    @Test
    public void testDoFilterSetStatusTooManyRequests() throws ServletException, IOException {
        filter.doFilter(request, response, filterChain);

        verify(response).setStatus(eq(TOO_MANY_REQUESTS_CODE));
    }

    @Test
    public void testDoFilterAllowedIp() throws ServletException, IOException {
        when(request.getRemoteAddr()).thenReturn(IP);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}