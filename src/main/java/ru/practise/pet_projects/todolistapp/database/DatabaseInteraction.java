package ru.practise.pet_projects.todolistapp.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.practise.pet_projects.todolistapp.handlers.Task;
import ru.practise.pet_projects.todolistapp.handlers.User;

import java.sql.*;

public class DatabaseInteraction {
    private static Connection connection;
    public static final String USER_INFO_SELECT_BY_LOGIN = """
            SELECT * FROM users
            WHERE login = (?);
            """;
    public static final String USER_INFO_SELECT_BY_USERNAME = """
            SELECT * FROM users
            WHERE username = (?);
            """;
    public static final String TASKS_INFO_SELECT_BY_USERNAME = """
            SELECT contents,priority,dedlines,status FROM tasks_content
            WHERE username = (?);
            """;
    public static final String INSERT_USER = "INSERT INTO users (login, password, username) VALUES (?, ?, ?)";
    public static final String INSERT_TASK = "INSERT INTO tasks_content (username, contents, priority, dedlines, status) " +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_ALL_TASKS = "DELETE FROM tasks_content WHERE username = (?)";
    public static final String DELETE_ALL_COMPLETED_TASKS = "DELETE FROM tasks_content " +
            "WHERE username = (?) AND status = 'выполнено'";
    public static final String EXECUTE_ALL_TASKS = "UPDATE tasks_content SET status = 'выполнено' " +
            "WHERE username = (?) AND status = 'не выполнено'";
    public static final String RENAME_TASK = "UPDATE tasks_content SET contents = (?) " +
            "WHERE username = (?) AND contents = (?) AND priority = (?) AND dedlines = (?)";
    public static final String DELETE_TASK = "DELETE FROM tasks_content " +
            "WHERE username = (?) AND contents = (?) AND priority = (?) AND dedlines = (?)";
    public static final String EXECUTE_TASK = "UPDATE tasks_content SET status = 'выполнено' " +
            "WHERE username = (?) AND contents = (?) AND priority = (?) AND dedlines = (?)";
    public static final String CANSEL_EXECUTION_TASK = "UPDATE tasks_content SET status = 'не выполнено' " +
            "WHERE username = (?) AND contents = (?) AND priority = (?) AND dedlines = (?)";

    public DatabaseInteraction() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ToDoListApp", "postgres", "5432");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Инициализация BD прошла успешно!!!");
    }

    public User getUsersInfo(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(USER_INFO_SELECT_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String loginResult = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("username");
                    return new User(loginResult, password, name);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void insertUser(String login, String password, String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean loginIsBusy(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(USER_INFO_SELECT_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean usernameIsBusy(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(USER_INFO_SELECT_BY_USERNAME)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void insertTask(String username, String content, String priority, String dedline, String done) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASK)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, content);
            preparedStatement.setString(3, priority);
            preparedStatement.setString(4, dedline);
            preparedStatement.setString(5, done);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Task> getTasks(String username) {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement(TASKS_INFO_SELECT_BY_USERNAME)) {
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
            throw new RuntimeException(e);
        }
        return tasks;
    }

    public void deleteAllTasks(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_TASKS)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllExecuteTasks(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_COMPLETED_TASKS)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeAllTasks(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(EXECUTE_ALL_TASKS)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void renameTask(String username, String newContent, Task selectedTask) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(RENAME_TASK)) {
            preparedStatement.setString(1, newContent);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, selectedTask.getContent());
            preparedStatement.setString(4, selectedTask.getPriority());
            preparedStatement.setString(5, selectedTask.getDedline());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeTask(String username, Task selectedTask) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TASK)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, selectedTask.getContent());
            preparedStatement.setString(3, selectedTask.getPriority());
            preparedStatement.setString(4, selectedTask.getDedline());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeTask(String username, Task selectedTask) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(EXECUTE_TASK)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, selectedTask.getContent());
            preparedStatement.setString(3, selectedTask.getPriority());
            preparedStatement.setString(4, selectedTask.getDedline());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void canselExecutionTask(String username, Task selectedTask) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CANSEL_EXECUTION_TASK)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, selectedTask.getContent());
            preparedStatement.setString(3, selectedTask.getPriority());
            preparedStatement.setString(4, selectedTask.getDedline());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
