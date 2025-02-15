package ru.practise.pet_projects.todolistapp.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.practise.pet_projects.todolistapp.handlers.Task;
import ru.practise.pet_projects.todolistapp.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


/**
 * The {@code DatabaseInteraction} class is responsible for managing interactions
 * with the database for the ToDoList application. It provides methods to execute
 * various SQL queries related to user and task management.
 */
public class DatabaseInteraction {
    public static final Logger LOGGER = LogManager.getLogger(DatabaseInteraction.class);
    private static Connection connection;
    private static final Properties QUERIES = new Properties();
    public static final Util properties = new Util();

    /**
     * Constructs a new instance of the DatabaseInteraction class.
     * This constructor attempts to establish a connection to a PostgreSQL database
     * using the specified URL, username, and password. If the connection is successful,
     * a message indicating successful initialization is printed to the console.
     *
     * @throws RuntimeException If there is an error establishing the database connection.
     */
    public DatabaseInteraction() {
        try {
            connection = DriverManager.getConnection(properties.getPropertiesValue("DATABASE_URL"),
                    properties.getPropertiesValue("DATABASE_USER"),
                    properties.getPropertiesValue("DATABASE_PASSWORD"));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        LOGGER.info("Инициализация BD прошла успешно!!!");
        getSQLReaquest();
    }

    /**
     * <p>This method attempts to read a file named {@code queries.sql} from the classpath.
     * If the file is not found, an {@code IOException} is thrown. The queries are loaded
     * into a {@code Properties} object called {@code QUERIES} using UTF-8 encoding.</p>
     *
     * <p>In case of any {@code IOException} during the loading process, an error message
     * is logged using {@code LOGGER}, and a {@code RuntimeException} is thrown to indicate
     * the failure to load SQL queries.</p>
     *
     * @throws RuntimeException if the SQL queries cannot be loaded due to an {@code IOException}.
     */
    private void getSQLReaquest() {
        try (InputStream input = getClass().getResourceAsStream("queries.sql")) {
            if (input == null) {
                throw new IOException("Файл не найден");
            }
            try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                QUERIES.load(reader);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Failed to load SQL queries", e);
        }
    }

    public static String getQuery(String key) {
        return QUERIES.getProperty(key);
    }

    /**
     * Retrieves user information from the database based on the provided {@code login}.
     * This method executes a SQL query to select user information associated with the given login.
     * If a user is found, it creates and returns a {@code User}.
     * If no user is found, it returns {@code null}.
     *
     * @param login The login of the user whose information is to be retrieved.
     * @return A {@code User} object containing the user's information, or null if no user is found.
     * @throws RuntimeException If there is an error executing the SQL query.
     */
    public String[] getUserInfo(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getQuery(NameOfSQLRequests.
                USER_INFO_SELECT_BY_LOGIN.getName()))) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String loginResult = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("username");
                    return new String[]{loginResult, password, name};
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return new String[0];
    }

    /**
     * Inserts a new user into the database with the provided {@code login}, {@code password}, and {@code username}.
     * This method prepares and executes an SQL statement to insert a new user record into the database.
     *
     * @param login    The login of the user to be inserted.
     * @param password The password of the user to be inserted.
     * @param username The username of the user to be inserted.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void insertUser(String login, String password, String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getQuery(NameOfSQLRequests.
                INSERT_USER.getName()))) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
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
        return isBusy(NameOfSQLRequests.
                USER_INFO_SELECT_BY_LOGIN, login);
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
        return isBusy(NameOfSQLRequests.
                USER_INFO_SELECT_BY_USERNAME, username);
    }

    private boolean isBusy(NameOfSQLRequests userInfoSelectByIdentifier, String identifier) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getQuery(
                userInfoSelectByIdentifier.getName()))) {
            preparedStatement.setString(1, identifier);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * This method prepares and executes an SQL statement to insert a new task record
     * into the database. If the insertion is successful, the task is added to the database.
     *
     * @param username The username of the user to whom the task belongs.
     * @param content  The content or description of the task.
     * @param priority The priority level of the task.
     * @param dedline  The deadline for the task.
     * @param done     The status of the task (e.g., completed or not).
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void insertTask(String username, String content, String priority, String dedline, String done) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getQuery(NameOfSQLRequests.
                INSERT_TASK.getName()))) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, content);
            preparedStatement.setString(3, priority);
            preparedStatement.setString(4, dedline);
            preparedStatement.setString(5, done);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of tasks parameters associated with the specified username from the database.
     * This method executes a SQL query to select all tasks belonging to the given user.
     * It creates and returns a list of {@code String[]} arrays containing the user's tasks parameters.
     *
     * @param username The username of the user whose tasks are to be retrieved.
     * @return {@link ArrayList} of {@code String[]} arrays containing the user's tasks parameters.
     * @throws RuntimeException If there is an error executing the SQL query.
     */
    public ArrayList<String[]> getTasks(String username) {
        var arrOfTasksParameters = new ArrayList<String[]>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getQuery(NameOfSQLRequests.
                TASKS_INFO_SELECT_BY_USERNAME.getName()))) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String content = resultSet.getString("contents");
                    String priority = resultSet.getString("priority");
                    String dedline = resultSet.getString("dedlines");
                    String status = resultSet.getString("status");
                    String[] taskParameters = {content, priority, dedline, status};
                    arrOfTasksParameters.add(taskParameters);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return arrOfTasksParameters;
    }

    /**
     * Renames an existing task in the database with the specified new content.
     * This method prepares and executes an SQL statement to update the content of a task.
     * The task to be renamed is identified by its current {@code content}, {@code priority}, and {@code deadline}.
     *
     * @param username     The username of the user who owns the task to be renamed.
     * @param newContent   The new content or description for the task.
     * @param selectedTask The Task object representing the task to be renamed, which contains
     *                     the current content, priority, and deadline of the task.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void renameTask(String username, String newContent, Task selectedTask) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getQuery(NameOfSQLRequests.
                RENAME_TASK.getName()))) {
            preparedStatement.setString(1, newContent);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, selectedTask.getContent());
            preparedStatement.setString(4, selectedTask.getPriority());
            preparedStatement.setString(5, selectedTask.getDedline());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes all tasks associated with the specified user from the database.
     * This method calls a helper method to execute the SQL statement for deleting all tasks.
     *
     * @param username The username of the user whose tasks are to be deleted.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void deleteAllTasks(String username) {
        requestForAllTasks(username, getQuery(NameOfSQLRequests.DELETE_ALL_TASKS.getName()));
    }

    /**
     * Deletes all completed tasks associated with the specified user from the database.
     * This method calls a helper method to execute the SQL statement for deleting all completed tasks.
     *
     * @param username The username of the user whose completed tasks are to be deleted.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void deleteAllExecuteTasks(String username) {
        requestForAllTasks(username, getQuery(NameOfSQLRequests.DELETE_ALL_COMPLETED_TASKS.getName()));
    }

    /**
     * Executes all tasks associated with the specified user in the database.
     * This method calls a helper method to execute the SQL statement for executing all tasks.
     *
     * @param username The username of the user whose tasks are to be executed.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void executeAllTasks(String username) {
        requestForAllTasks(username, getQuery(NameOfSQLRequests.EXECUTE_ALL_TASKS.getName()));
    }

    /**
     * This method calls a helper method to execute the SQL statement for deleting the specified task.
     *
     * @param username     The username of the user who owns the task to be removed.
     * @param selectedTask The {@code Task} representing the task to be removed.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void deleteTask(String username, Task selectedTask) {
        delAndExecuteTask(username, selectedTask, getQuery(NameOfSQLRequests.DELETE_TASK.getName()));
    }

    /**
     * This method calls a helper method to execute the SQL statement for executing the specified task.
     *
     * @param username     The username of the user who owns the task to be executed.
     * @param selectedTask The {@code Task} representing the task to be executed.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void executeTask(String username, Task selectedTask) {
        delAndExecuteTask(username, selectedTask, getQuery(NameOfSQLRequests.EXECUTE_TASK.getName()));
    }

    /**
     * This method calls a helper method to execute the SQL statement for canceling the execution of the specified task.
     *
     * @param username     The username of the user who owns the task to be canceled.
     * @param selectedTask The {@code Task} representing the task to be canceled.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void canselExecutionTask(String username, Task selectedTask) {
        delAndExecuteTask(username, selectedTask, getQuery(NameOfSQLRequests.CANSEL_EXECUTION_TASK.getName()));
    }

    /**
     * Executes a SQL statement to request all tasks associated with the specified username.
     * This method prepares and executes the SQL statement for the given request.
     *
     * @param username The username of the user whose tasks are to be requested.
     * @param request  The SQL statement to be executed.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    private void requestForAllTasks(String username, String request) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes a SQL statement to delete or execute a specific task associated with the specified user.
     * This method prepares and executes the SQL statement for the given request.
     *
     * @param username     The username of the user who owns the task.
     * @param selectedTask The {@code Task} representing the task to be deleted or executed.
     * @param request      The SQL statement to be executed.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    private void delAndExecuteTask(String username, Task selectedTask, String request) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(request)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, selectedTask.getContent());
            preparedStatement.setString(3, selectedTask.getPriority());
            preparedStatement.setString(4, selectedTask.getDedline());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}