package ru.practise.pet_projects.todolistapp.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.log4j.Log4j2;
import ru.practise.pet_projects.todolistapp.handlers.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;

@Log4j2
public class TaskInteraction {
    private static Connection connection;

    public TaskInteraction() {
        connection = DATABASE.connectWithDatabase();
    }

    public ObservableList<Task> getTasks(String username) {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.getUserTasksQuery())) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String content = resultSet.getString("contents");
                    String priority = resultSet.getString("priority");
                    String dedline = resultSet.getString("dedlines");
                    String status = resultSet.getString("status");
                    Task task = new Task(content, priority, dedline, status);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return tasks;
    }
}
