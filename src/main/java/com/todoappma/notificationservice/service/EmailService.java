package com.todoappma.notificationservice.service;

public interface EmailService {
    void sendReminderEmail(String to, String todoTitle, String deadline);
}
