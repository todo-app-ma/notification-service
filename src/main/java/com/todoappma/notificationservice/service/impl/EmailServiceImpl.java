package com.todoappma.notificationservice.service.impl;

import com.todoappma.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendReminderEmail(String to, String todoTitle, String deadline) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Reminder: " + todoTitle + " due in 1 hour");
            message.setText("Hi,\n\nThis is a reminder that your todo \"" + todoTitle
                    + "\" is due at " + deadline + ".\n\nDon't forget to complete it!\n\nTodo App");
            mailSender.send(message);
            log.info("Reminder email sent to {} for todo: {}", to, todoTitle);
        } catch (Exception e) {
            log.error("Failed to send reminder email to {}: {}", to, e.getMessage());
        }
    }
}
