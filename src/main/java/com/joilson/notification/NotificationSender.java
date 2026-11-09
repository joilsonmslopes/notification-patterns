package com.joilson.notification;

public interface NotificationSender {
    String channel();
    void send(String destination, String message);
}
