package com.joilson.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NotificationSenderRegistryTest {

    @Test
    void shouldRejectDuplicateChannels() {
        List<NotificationSender> implementations = List.of(
                new EmailNotificationSender(),
                new EmailNotificationSender()
        );

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> new NotificationSenderRegistry(implementations)
        );
    }

    @Test
    void shouldReturnRegisteredSender() {
        NotificationSender emailNotificationSender = new EmailNotificationSender();
        NotificationSender smsNotificationSender = new SmsNotificationSender();

        List<NotificationSender> implementations = List.of(
                emailNotificationSender,
                smsNotificationSender
        );

        NotificationSenderRegistry registry = new NotificationSenderRegistry(implementations);

        NotificationSender selected = registry.getSender("sms");

        Assertions.assertSame(smsNotificationSender, selected);
    }

    @Test
    void shouldRejectUnsupportedChannel() {
        List<NotificationSender> implementations = List.of(
                new EmailNotificationSender(),
                new SmsNotificationSender()
        );

        NotificationSenderRegistry registry = new NotificationSenderRegistry(implementations);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> registry.getSender("whatsapp")
        );
    }
}
