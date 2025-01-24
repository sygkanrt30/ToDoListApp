package ru.practise.pet_projects.todolistapp.handlers;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class Task {
    private final SimpleStringProperty content;
    private final SimpleStringProperty priority;
    private final SimpleStringProperty dedline;
    private final SimpleStringProperty status;

    public Task(String content, String priority, String dedline, String status) {
        this.content = new SimpleStringProperty(content);
        this.priority = new SimpleStringProperty(priority);
        this.dedline = new SimpleStringProperty(dedline);
        this.status = new SimpleStringProperty(status);
    }

    public String getContent() {
        return content.get();
    }

    public String getPriority() {
        return priority.get();
    }

    public String getDedline() {
        return dedline.get();
    }

    public String getStatus() {
        return status.get();
    }

    @Override
    public String toString() {
        return "Task{" +
                "content='" + content + '\'' +
                ", priority='" + priority + '\'' +
                ", dedline=" + dedline +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(content, task.content) && Objects.equals(priority, task.priority) && Objects.equals(dedline, task.dedline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, priority, dedline);
    }
}
