package com.todoappma.notificationservice.repository;

import com.todoappma.notificationservice.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReminderRepository extends JpaRepository<Reminder, UUID> {
    List<Reminder> findBySentFalseAndReminderTimeLessThanEqual(LocalDateTime now);
    boolean existsByTodoId(UUID todoId);
}
