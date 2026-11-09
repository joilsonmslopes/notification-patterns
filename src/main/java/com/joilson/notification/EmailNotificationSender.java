package com.joilson.notification;

public class EmailNotificationSender implements NotificationSender {
    private static final String CHANNEL = "email";

    @Override
    public String channel() {
        return CHANNEL;
    }

    @Override
    public void send(String destination, String message) {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("O Destinatário é obrigatório.");
        }

        if (!destination.contains("@")) {
            throw new IllegalArgumentException("Insira um e-mail válido.");
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("A Mensagem é obrigatória.");
        }

        NotificationConsolePrinter.printNotification(CHANNEL, destination, message);
    }
}
