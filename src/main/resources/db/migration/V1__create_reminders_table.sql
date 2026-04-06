CREATE TABLE reminders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    todo_id UUID NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    todo_title VARCHAR(255) NOT NULL,
    deadline TIMESTAMP NOT NULL,
    reminder_time TIMESTAMP NOT NULL,
    sent BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_reminders_sent_reminder_time ON reminders(sent, reminder_time);
