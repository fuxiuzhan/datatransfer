package com.fxz.channelswitcher.datatransferserver.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * limiter
 */
public class TpsLimiter {
    private Long timeStart;
    private int internal;
    private AtomicInteger counter;
    private int token;

    public TpsLimiter(int internal, int token) {
        this(System.currentTimeMillis(), internal, token);
    }

    /**
     * @param timeStart ms
     * @param internal  s
     * @param token
     */
    public TpsLimiter(Long timeStart, int internal, int token) {
        this.timeStart = timeStart;
        this.internal = internal;
        counter = new AtomicInteger(token);
        this.token = token;
    }

    public boolean isAllow() {
        if (System.currentTimeMillis() - timeStart > internal) {
            timeStart = System.currentTimeMillis();
            counter = new AtomicInteger(token);
        }
        if (counter.decrementAndGet() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
