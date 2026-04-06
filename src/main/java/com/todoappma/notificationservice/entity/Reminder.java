package com.todoappma.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reminders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reminder extends BaseEntity {

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID todoId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String todoTitle;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private LocalDateTime reminderTime;

    @Column(nullable = false)
    @Builder.Default
    private boolean sent = false;
}
