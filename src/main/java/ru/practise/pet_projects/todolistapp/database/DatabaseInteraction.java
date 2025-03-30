package ru.practise.pet_projects.todolistapp.database;

import lombok.extern.log4j.Log4j2;
import ru.practise.pet_projects.todolistapp.handlers.Task;
import ru.practise.pet_projects.todolistapp.utils.PropertyReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Log4j2
public class DatabaseInteraction {
    private static Connection connection;
    private static final PropertyReader PROPERTIES = new PropertyReader("ToDoListApp.properties");

    public DatabaseInteraction() {
        connection = connectWithDatabase();
        log.info("Инициализация BD прошла успешно!!!");
    }

    public Connection connectWithDatabase() {
        try {
            connection = DriverManager.getConnection(PROPERTIES.get("DATABASE_URL"),
                    PROPERTIES.get("DATABASE_USER"),
                    PROPERTIES.get("DATABASE_PASSWORD"));
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void insertUser(String login, String password, String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.getUserCreationQuery())) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void insertTask(String username, String content, String priority, String dedline, String done) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.getTaskCreationQuery())) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, content);
            preparedStatement.setString(3, priority);
            preparedStatement.setString(4, dedline);
            preparedStatement.setString(5, done);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void renameTask(String username, String newContent, Task selectedTask) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.getRenameTaskQuery())) {
            preparedStatement.setString(1, newContent);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, selectedTask.getContent());
            preparedStatement.setString(4, selectedTask.getPriority());
            preparedStatement.setString(5, selectedTask.getDedline());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void deleteAllTasks(String username) {
        requestForAllTasks(username, SqlQueries.getRemoveAllTasksQuery());
    }

    public void deleteAllExecuteTasks(String username) {
        requestForAllTasks(username, SqlQueries.getRemoveAllCompletedTasksQuery());
    }

    public void executeAllTasks(String username) {
        requestForAllTasks(username, SqlQueries.getExecuteAllTasksQuery());
    }

    public void deleteTask(String username, Task selectedTask) {
        delAndExecuteTask(username, selectedTask, SqlQueries.getRemoveTaskQuery());
    }


    public void executeTask(String username, Task selectedTask) {
        delAndExecuteTask(username, selectedTask, SqlQueries.getExecuteTaskQuery());
    }

    public void canselExecutionTask(String username, Task selectedTask) {
        delAndExecuteTask(username, selectedTask, SqlQueries.getCanselExecutionTaskQuery());
    }

    private void requestForAllTasks(String username, String request) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void delAndExecuteTask(String username, Task selectedTask, String request) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, selectedTask.getContent());
            preparedStatement.setString(3, selectedTask.getPriority());
            preparedStatement.setString(4, selectedTask.getDedline());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}