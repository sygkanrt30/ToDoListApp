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

    /**
     * Retrieves user information from the database based on the provided {@code login}.
     * This method executes a SQL query to select user information associated with the given login.
     * If a user is found, it creates and returns a {@code User}.
     * If no user is found, it returns {@code null}.
     *
     * @param identifier The login or username of the user whose information is to be retrieved.
     * @return A {@code User} object containing the user's information, or null if no user is found.
     * @throws RuntimeException If there is an error executing the SQL query.
     */
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

    /**
     * This method executes a SQL query to determine if a user with the given {@code login}
     * exists in the database.
     *
     * @param login The login to check for availability.
     * @return true if the login is available, false if it is already in use.
     * @throws RuntimeException If there is an error executing the SQL query.
     */
    public boolean loginIsBusy(String login) {
        return (getUserInfo(SqlQueries.getUserByLoginQuery(), login) == null);
    }

    /**
     * This method executes a SQL query to determine if a user with the given {@code username}
     * exists in the database.
     *
     * @param username The username to check for availability.
     * @return true if the username is available, false if it is already in use.
     * @throws RuntimeException If there is an error executing the SQL query.
     */
    public boolean usernameIsBusy(String username) {
        return (getUserInfo(SqlQueries.getUserByUsernameQuery(), username) == null);
    }

}
