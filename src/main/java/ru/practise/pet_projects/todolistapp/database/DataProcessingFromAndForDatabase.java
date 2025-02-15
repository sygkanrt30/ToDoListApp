package ru.practise.pet_projects.todolistapp.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.practise.pet_projects.todolistapp.handlers.Task;
import ru.practise.pet_projects.todolistapp.handlers.User;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;

/**
 * A utility class for processing data from and to the database.
 * This class provides methods to retrieve and convert data related to tasks and users.
 */
public class DataProcessingFromAndForDatabase {
    /**
     * <p>This method queries the database for tasks that belong to the given {@code username}
     * and returns an {@code ArrayList} of {@code String[]} arrays, where each array contains the
     * task parameters such as content, priority, deadline, and status.</p>
     *
     * @param username the {@code String} representing the username for which tasks are to be retrieved.
     * @return an {@code ArrayList<String[]>} containing task parameters for the specified username.
     * Each {@code String[]} array contains the following elements:
     * <ul>
     * <li>{@code content} - the content of the task</li>
     * <li>{@code priority} - the priority of the task</li>
     * <li>{@code dedline} - the deadline of the task</li>
     * <li>{@code status} - the current status of the task</li>
     * </ul>
     * @throws RuntimeException if a {@code SQLException} occurs while accessing the database.
     */
    public static ObservableList<Task> makeListOfTableElements(String username) {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        for (String[] arr : DATABASE.getTasks(username)) {
            Task task = new Task(arr[0], arr[1], arr[2], arr[3]);
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * <p>This method retrieves user information from the database for the given {@code login}
     * and creates a {@code User} object using the retrieved data. If the retrieved data does not
     * contain sufficient information (i.e., the length of the data array is less than or equal to 1),
     * the method returns {@code null}.</p>
     *
     * @param login the {@code String} representing the user's login information.
     * @return a {@code User} object containing the user's data if available; {@code null} if
     * insufficient data is found.
     */
    public static User convertArrayOfDataForUser(String login) {
        String[] data = DATABASE.getUserInfo(login);
        if (data.length > 1) {
            return new User(data[0], data[1], data[2]);
        }
        return null;
    }
}


