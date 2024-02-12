package com.challenge.notificator;

public interface Notificator {
    void send(String type, String userId, String message);
    int getCounter();
}
