package ru.practise.pet_projects.todolistapp.database;

import lombok.extern.log4j.Log4j2;
import ru.practise.pet_projects.todolistapp.handlers.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;

@Log4j2
public class UserInteraction {
    private static Connection connection;

    public UserInteraction() {
        connection = DATABASE.connectWithDatabase();
    }

    public User getUserInfo(String query, String identifier) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, identifier);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String loginResult = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("username");
                    return new User(loginResult, password, name);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean loginIsBusy(String login) {
        return (getUserInfo(SqlQueries.getUserByLoginQuery(), login) == null);
    }

    public boolean usernameIsBusy(String username) {
        return (getUserInfo(SqlQueries.getUserByUsernameQuery(), username) == null);
    }
}
