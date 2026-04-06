package com.todoappma.notificationservice.service;

import com.todoappma.notificationservice.entity.Reminder;
import com.todoappma.notificationservice.repository.ReminderRepository;
import com.todoappma.notificationservice.service.impl.ReminderScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderSchedulerTest {

    @Mock private ReminderRepository reminderRepository;
    @Mock private EmailService emailService;

    @InjectMocks
    private ReminderScheduler scheduler;

    @Test
    void sendDueReminders_shouldSendEmailAndMarkSent() {
        var reminder = Reminder.builder()
                .userId(UUID.randomUUID())
                .todoId(UUID.randomUUID())
                .userEmail("user@test.com")
                .todoTitle("Buy groceries")
                .deadline(LocalDateTime.now().plusMinutes(30))
                .reminderTime(LocalDateTime.now().minusMinutes(1))
                .sent(false)
                .build();

        when(reminderRepository.findBySentFalseAndReminderTimeLessThanEqual(any()))
                .thenReturn(List.of(reminder));

        scheduler.sendDueReminders();

        verify(emailService).sendReminderEmail("user@test.com", "Buy groceries", reminder.getDeadline().toString());
        verify(reminderRepository, times(2)).save(any());
    }

    @Test
    void sendDueReminders_shouldDoNothing_whenNoRemindersAreDue() {
        when(reminderRepository.findBySentFalseAndReminderTimeLessThanEqual(any()))
                .thenReturn(List.of());

        scheduler.sendDueReminders();

        verifyNoInteractions(emailService);
    }
}
