package com.joilson.notification;

public class NotificationConsolePrinter {
    public static void printNotification(String channel, String destination, String message) {
        System.out.println("Enviando " + channel + "...");
        System.out.println("Destinatário: " + destination);
        System.out.println("Mensagem: " + message);
    }
}

