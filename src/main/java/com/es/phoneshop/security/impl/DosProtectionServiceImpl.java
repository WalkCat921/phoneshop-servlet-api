package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionServiceImpl implements DosProtectionService {
    private static final long THRESHOLD = 20;
    private static final int TIMER_UPDATE_MILLISECOND = 60000;
    private static final int DELAY_MILLISECOND = 1500;

    private final Timer timer;
    private static volatile DosProtectionService instance;
    private Map<String, Long> countMap = new ConcurrentHashMap();

    private DosProtectionServiceImpl() {
        timer = new Timer();
        timer.scheduleAtFixedRate(getTimerTask(), DELAY_MILLISECOND, TIMER_UPDATE_MILLISECOND);
    }

    public static DosProtectionService getInstance() {
        if (instance == null) {
            synchronized (DosProtectionService.class) {
                if (instance == null) {
                    instance = new DosProtectionServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                countMap.clear();
            }
        };
    }
}
