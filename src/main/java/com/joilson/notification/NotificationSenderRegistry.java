package com.joilson.notification;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NotificationSenderRegistry {
    private final Map<String, NotificationSender> senders;

    public NotificationSenderRegistry(List<NotificationSender> implementations) {
        Map<String, NotificationSender> registered = new LinkedHashMap<>();

        for (var sender : implementations) {
            var id = sender.channel();

            if (registered.putIfAbsent(id, sender) != null) {
                throw new IllegalStateException("NotificationSender duplicado com a chave: " + id);
            }
        }

        senders = Map.copyOf(registered);
    }

    public NotificationSender getSender(String channel) {
        var sender = senders.get(channel);
        if (sender == null) {
            throw new IllegalArgumentException("Canal não suportado: " + channel);
        }
        return sender;
    }
}
