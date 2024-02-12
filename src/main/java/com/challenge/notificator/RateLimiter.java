package com.challenge.notificator;

import lombok.Data;

import java.time.Instant;

@Data
public class RateLimiter {

    private int capacity;
    private int windowTimeInSeconds;
    private long availableTokens;
    private Instant lastRefillTimeStamp;
    private double refillCountPerSecond;


    public RateLimiter(int capacity, int windowTimeInSeconds) {
        this.capacity = capacity;
        this.windowTimeInSeconds = windowTimeInSeconds;
        lastRefillTimeStamp = Instant.now();
        refillCountPerSecond = (double) capacity / windowTimeInSeconds;
        availableTokens = capacity;
    }

    public boolean tryConsume(){
        refill();

        if(availableTokens > 0)
        {
            --availableTokens;
            return true;
        }
        else
        {
            return false;
        }
    }

    private void refill(){
        var now = Instant.now();
        if(now.getEpochSecond() > lastRefillTimeStamp.getEpochSecond())
        {
            long elapsedTime = now.getEpochSecond() - lastRefillTimeStamp.getEpochSecond();
            //refill tokens for this durationlong
            var tokensToBeAdded = (elapsedTime/1000) * refillCountPerSecond;
            if(tokensToBeAdded > 0) {
                availableTokens = Math.min(capacity, availableTokens + (int)tokensToBeAdded);
                lastRefillTimeStamp = now;
            }
        }
    }
}
