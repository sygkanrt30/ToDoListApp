package ru.practise.pet_projects.todolistapp.handlers;

import javafx.beans.property.SimpleStringProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


/**
 * The {@code Task} class represents a task in a to-do list application.
 * Each task has content, priority, deadline, and status properties.
 * It provides methods to access these properties, as well as methods
 * for equality comparison and string representation.
 */
@ToString
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class Task {
    private final SimpleStringProperty content;
    private final SimpleStringProperty priority;
    private final SimpleStringProperty dedline;
    private final SimpleStringProperty status;

    /**
     * Constructs a new Task instance with the specified {@code content}, {@code priority}, {@code dedline}, and {@code status}.
     *
     * @param content  The content of the task.
     * @param priority The priority level of the task.
     * @param dedline  The deadline for the task.
     * @param status   The current status of the task.
     */
    public Task(String content, String priority, String dedline, String status) {
        this.content = new SimpleStringProperty(content);
        this.priority = new SimpleStringProperty(priority);
        this.dedline = new SimpleStringProperty(dedline);
        this.status = new SimpleStringProperty(status);
    }
}
