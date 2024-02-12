package com.challenge.notificator;

import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mockStatic;


class NotificatorApplicationTests {

    @Test
    void shouldSend2PerMinute() {

        String instantExpected = "2024-02-11T10:15:00Z";
        mockInstant(instantExpected);
        var statusRateLimiter = new RateLimiter(2, 10);
        var notificator = new RateAwareNotificator(Map.of("status", statusRateLimiter));
        notificator.send("status", "user", "news 1");
        String instantExpected2 = "2024-02-11T10:17:30Z";
        mockInstant(instantExpected2);
        notificator.send("status", "user", "news 2");
        String instantExpected3 = "2024-02-11T10:19:31Z";
        mockInstant(instantExpected3);
        notificator.send("status", "user", "news 3");
        assertThat(notificator.getCounter()).isEqualTo(2);



    }

    @Test
    void shouldSend1PerDay() {
        var newsRateLimiter = new RateLimiter(1, 86400);
        var notificator = new RateAwareNotificator(Map.of("news", newsRateLimiter));
        String instantExpected = "2024-02-11T10:15:00Z";
        mockInstant(instantExpected);
        notificator.send("news", "user", "news 1");
        instantExpected = "2024-02-12T10:15:00Z";
        mockInstant(instantExpected);
        notificator.send("news", "user", "news 2");
        assertThat(notificator.getCounter()).isEqualTo(1);
    }

    @Test
    void shouldSend3PerHour() {
        var marketingRateLimiter = new RateLimiter(3, 10800);
        var notificator = new RateAwareNotificator(Map.of("marketing", marketingRateLimiter));
        String instantExpected = "2024-02-11T10:15:00Z";
        mockInstant(instantExpected);
        notificator.send("marketing", "user", "news 1");
        notificator.send("marketing", "user", "news 1");
        String instantExpected2 = "2024-02-11T11:15:00Z";
        mockInstant(instantExpected2);
        notificator.send("marketing", "user", "news 2");
        String instantExpected3 = "2024-02-11T12:15:00Z";
        mockInstant(instantExpected3);
        notificator.send("marketing", "user", "news 3");
        assertThat(notificator.getCounter()).isEqualTo(3);
    }

    private void mockInstant(String instantExpected) {
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
        Instant instant = Instant.now(clock);
        new MockUp<Instant>() {
            @Mock
            public Instant now() {
                return Instant.now(clock);
            }
        };
    }

}
