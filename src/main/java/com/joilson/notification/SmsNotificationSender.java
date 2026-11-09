package com.joilson.notification;

public class SmsNotificationSender implements NotificationSender {

    private static final String CHANNEL = "sms";

    @Override
    public String channel() {
        return CHANNEL;
    }

    @Override
    public void send(String destination, String message) {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("O Destinatário é obrigatório");
        }

        if (!destination.matches("^\\d{11}")) {
            throw new IllegalArgumentException("O Destinatário aceita apenas digitos numéricos e deve conter apenas 11 caractéres");
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("A Mensagem é obrigatória.");
        }

        NotificationConsolePrinter.printNotification(CHANNEL, destination, message);
    }
}
