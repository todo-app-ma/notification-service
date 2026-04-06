package com.todoappma.notificationservice.service.impl;

import com.todoappma.notificationservice.repository.ReminderRepository;
import com.todoappma.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ReminderRepository reminderRepository;
    private final EmailService emailService;

    @Scheduled(cron = "${reminder.scheduler.cron}")
    @Transactional
    public void sendDueReminders() {
        var dueReminders = reminderRepository.findBySentFalseAndReminderTimeLessThanEqual(LocalDateTime.now());

        if (dueReminders.isEmpty()) {
            return;
        }

        log.info("Found {} due reminders to send", dueReminders.size());

        for (var reminder : dueReminders) {
            emailService.sendReminderEmail(
                    reminder.getUserEmail(),
                    reminder.getTodoTitle(),
                    reminder.getDeadline().toString()
            );
            reminder.setSent(true);
            reminderRepository.save(reminder);
        }
    }
}
