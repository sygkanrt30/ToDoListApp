package ru.practise.pet_projects.todolistapp.database;

import ru.practise.pet_projects.todolistapp.user.User;

import java.sql.*;

public class DatabaseInteraction {
    private static Connection connection;
    public static final String USER_INFO_SELECT = """
            SELECT * FROM users
            WHERE login = (?);
            """;
    public static final String INSERT_USER = "INSERT INTO users (login, password, username) VALUES (?, ?, ?)";

    public DatabaseInteraction() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ToDoListApp", "postgres", "5432");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Инициализация BD прошла успешно!!!");
    }

    public User getUsersInfo(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(USER_INFO_SELECT)) {
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(USER_INFO_SELECT)) {
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
}
