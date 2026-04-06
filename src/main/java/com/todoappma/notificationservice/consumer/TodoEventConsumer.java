package com.todoappma.notificationservice.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoappma.notificationservice.entity.Reminder;
import com.todoappma.notificationservice.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodoEventConsumer {

    private final ReminderRepository reminderRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${reminder.topic}", groupId = "notification-service")
    public void consume(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);

            // Debezium wraps payload in an envelope
            JsonNode payload = root.has("payload") ? root.get("payload") : root;
            String eventType = payload.path("eventType").asText();

            if (!"TodoCreated".equals(eventType)) {
                return;
            }

            JsonNode eventPayload = objectMapper.readTree(payload.path("payload").asText());

            UUID todoId = UUID.fromString(eventPayload.path("todoId").asText());

            if (reminderRepository.existsByTodoId(todoId)) {
                log.info("Reminder already exists for todoId: {}", todoId);
                return;
            }

            LocalDateTime deadline = LocalDateTime.parse(eventPayload.path("deadline").asText());
            LocalDateTime reminderTime = deadline.minusHours(1);

            if (reminderTime.isBefore(LocalDateTime.now())) {
                log.info("Deadline too soon for reminder, skipping todoId: {}", todoId);
                return;
            }

            Reminder reminder = Reminder.builder()
                    .userId(UUID.fromString(eventPayload.path("userId").asText()))
                    .todoId(todoId)
                    .userEmail(eventPayload.path("userEmail").asText(""))
                    .todoTitle(eventPayload.path("title").asText())
                    .deadline(deadline)
                    .reminderTime(reminderTime)
                    .sent(false)
                    .build();

            reminderRepository.save(reminder);
            log.info("Reminder scheduled for todoId: {} at {}", todoId, reminderTime);

        } catch (Exception e) {
            log.error("Failed to process todo event: {}", e.getMessage());
        }
    }
}
