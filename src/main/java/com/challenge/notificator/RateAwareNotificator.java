package com.challenge.notificator;

import java.util.Map;

public class RateAwareNotificator implements Notificator {
    private Map<String, RateLimiter> rateLimiterMap;
    private Gateway gateway;
    private int counter;

    public RateAwareNotificator() {
        this.gateway = new Gateway();
    }

    public RateAwareNotificator(Map<String, RateLimiter> rateLimiterMap) {
        this();
        this.rateLimiterMap = rateLimiterMap;
    }

    @Override
    public void send(String type, String userId, String message) {
        var limiter = rateLimiterMap.get(type);
        if (limiter.tryConsume()) {
            counter++;
            gateway.send(userId, message);
        }
    }

    @Override
    public int getCounter() {
        return this.counter;
    }
}
