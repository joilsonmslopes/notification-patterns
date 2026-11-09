package com.joilson.notification;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        NotificationSenderRegistry notificationSenderRegistry = new NotificationSenderRegistry(
                List.of(
                        new EmailNotificationSender(),
                        new SmsNotificationSender()
                )
        );

        NotificationSender emailNotificationSender = notificationSenderRegistry.getSender("email");
        NotificationSender smsNotificationSender = notificationSenderRegistry.getSender("sms");

        emailNotificationSender.send("joilson@email.com.br", "mensagem enviada por e-mail");

        System.out.println("=================================");

        smsNotificationSender.send("11912341234", "Seu código é 123456");
    }
}