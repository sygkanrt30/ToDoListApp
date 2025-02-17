package ru.practise.pet_projects.todolistapp.database;

import lombok.extern.log4j.Log4j2;
import ru.practise.pet_projects.todolistapp.handlers.Task;
import ru.practise.pet_projects.todolistapp.utils.PropertyReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * The {@code DatabaseInteraction} class is responsible for managing interactions
 * with the database for the ToDoList application. It provides methods to execute
 * various SQL queries related to user and task management.
 */
@Log4j2
public class DatabaseInteraction {
    private static Connection connection;
    private static final PropertyReader PROPERTIES = new PropertyReader("queries.sql");

    /**
     * Constructs a new instance of the DatabaseInteraction class.
     * This constructor attempts to establish a connection to a PostgreSQL database
     * using the specified URL, username, and password. If the connection is successful,
     * a message indicating successful initialization is printed to the console.
     *
     * @throws RuntimeException If there is an error establishing the database connection.
     */
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.getRenameTaskQuery())) {
            preparedStatement.setString(1, newContent);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, String.valueOf(selectedTask.getContent()));
            preparedStatement.setString(4, String.valueOf(selectedTask.getPriority()));
            preparedStatement.setString(5, String.valueOf(selectedTask.getDedline()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
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
        requestForAllTasks(username, SqlQueries.getRemoveAllTasksQuery());
    }

    /**
     * Deletes all completed tasks associated with the specified user from the database.
     * This method calls a helper method to execute the SQL statement for deleting all completed tasks.
     *
     * @param username The username of the user whose completed tasks are to be deleted.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void deleteAllExecuteTasks(String username) {
        requestForAllTasks(username, SqlQueries.getRemoveAllCompletedTasksQuery());
    }

    /**
     * Executes all tasks associated with the specified user in the database.
     * This method calls a helper method to execute the SQL statement for executing all tasks.
     *
     * @param username The username of the user whose tasks are to be executed.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void executeAllTasks(String username) {
        requestForAllTasks(username, SqlQueries.getExecuteAllTasksQuery());
    }

    /**
     * This method calls a helper method to execute the SQL statement for deleting the specified task.
     *
     * @param username     The username of the user who owns the task to be removed.
     * @param selectedTask The {@code Task} representing the task to be removed.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void deleteTask(String username, Task selectedTask) {
        delAndExecuteTask(username, selectedTask, SqlQueries.getRemoveTaskQuery());
    }

    /**
     * This method calls a helper method to execute the SQL statement for executing the specified task.
     *
     * @param username     The username of the user who owns the task to be executed.
     * @param selectedTask The {@code Task} representing the task to be executed.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void executeTask(String username, Task selectedTask) {
        delAndExecuteTask(username, selectedTask, SqlQueries.getExecuteTaskQuery());
    }

    /**
     * This method calls a helper method to execute the SQL statement for canceling the execution of the specified task.
     *
     * @param username     The username of the user who owns the task to be canceled.
     * @param selectedTask The {@code Task} representing the task to be canceled.
     * @throws RuntimeException If there is an error executing the SQL statement.
     */
    public void canselExecutionTask(String username, Task selectedTask) {
        delAndExecuteTask(username, selectedTask, SqlQueries.getCanselExecutionTaskQuery());
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
            log.error(e.getMessage(), e);
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
            preparedStatement.setString(2, String.valueOf(selectedTask.getContent()));
            preparedStatement.setString(3, String.valueOf(selectedTask.getPriority()));
            preparedStatement.setString(4, String.valueOf(selectedTask.getDedline()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}