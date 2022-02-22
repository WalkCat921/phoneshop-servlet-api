package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DosProtectionServiceImplTest {

    @Spy
    private ConcurrentHashMap<String, Long> countMap;

    @InjectMocks
    private DosProtectionServiceImpl dosProtectionService;

    private static final int TIMER_UPDATE_MILLISECOND = 60000;
    private static final String IP = "127.0.0.1";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testSingletonService() {
        DosProtectionService first;
        DosProtectionService second;

        first = DosProtectionServiceImpl.getInstance();
        second = DosProtectionServiceImpl.getInstance();

        assertSame(first, second);
    }

    @Test
    public void testIsIpAllowedFalse() {
        final Long connectTimes = 21L;

        countMap.put(IP,connectTimes);

        assertFalse(dosProtectionService.isAllowed(IP));
    }

    @Test
    public void testIsIpAllowedTrue() {
        final Long connectTimes = 2L;

        countMap.put(IP,connectTimes);

        assertTrue(dosProtectionService.isAllowed(IP));
    }

    @Test
    public void testIsIpAllowedTrueIfCountZero() {
        final Long connectTimes = 0L;

        countMap.put(IP,connectTimes);

        assertTrue(dosProtectionService.isAllowed(IP));
    }

    @Test
    public void testMapClearingByTimer() throws InterruptedException {
        final Long connectTimes = 31L;

        countMap.put(IP, connectTimes);
        Thread.sleep(TIMER_UPDATE_MILLISECOND);

        assertTrue(dosProtectionService.isAllowed(IP));
    }

    @Test
    public void testOtherIpIsAllowedWhenOneIpBlocked() throws InterruptedException {
        final Long connectTimes = 31L;
        final String anotherIP = "127.0.0.2";

        countMap.put(IP, connectTimes);

        assertTrue(dosProtectionService.isAllowed(anotherIP));
    }
}