package ru.practise.pet_projects.todolistapp.handlers;

import javafx.beans.property.SimpleStringProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
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
}
