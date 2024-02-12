package com.challenge.notificator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class NotificatorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NotificatorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var statusRateLimiter = new RateLimiter(2, 60);
        var newsRateLimiter =new  RateLimiter(1, 86400);
        var marketinRateLimiter = new  RateLimiter(3, 3600);
        var notificator = new RateAwareNotificator(Map.of("status", statusRateLimiter,
                "news", newsRateLimiter,
                "marketing", marketinRateLimiter));
        notificator.send("status", "user", "news 1");
        notificator.send("status", "user", "news 2");
        notificator.send("status", "user", "news 3");

    }
}
