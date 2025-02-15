package ru.practise.pet_projects.todolistapp.main_part;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.practise.pet_projects.todolistapp.MainApplication;
import ru.practise.pet_projects.todolistapp.database.DataProcessingFromAndForDatabase;
import ru.practise.pet_projects.todolistapp.handlers.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;

/**
 * The {@code MainBodyController} class is responsible for managing the user interface
 * and interactions of a To-Do List application. It handles task creation, execution,
 * renaming, and deletion, as well as updating the user interface based on user actions.
 *
 * <p>This class utilizes JavaFX components for building the GUI and interacts with
 * a database to persist task information.</p>
 */
public class MainBodyController {
    private String stringPriority;
    private String username;
    private static final String COMPLETED = "выполнено";
    private static final String NOT_COMPLETED = "не выполнено";
    private Task selectedTask;
    public static final Logger LOGGER = LogManager.getLogger(MainBodyController.class);
    @FXML
    private Button buttonCreateTask, buttonExecuteAllTasks, buttonRemoveAllTCompletedTasks, buttonRemoveAllTasks, buttonExit,
            buttonExecute, buttonRemove, buttonRename;

    @FXML
    private DatePicker dedline;

    @FXML
    private Label greeting, notCorrectFieldFill, notCorrectRenameFieldFill, taskNotSelected;

    @FXML
    private TableColumn<Task, StringConverter<LocalDate>> dedlinesColumn;

    @FXML
    private TableColumn<Task, String> prioritiesColumn, tasksColumn, statusColumn;

    @FXML
    private MenuButton priority;

    @FXML
    private TableView<Task> table;

    @FXML
    private TextField task, renameTask;


    /**
     * This method retrieves the content, priority, and deadline from the user input fields.
     * It checks if the content is not empty and if a task with the same content, priority,
     * and deadline already exists using the {@code isSameTask} method. If the task is unique,
     * it inserts the new task into the database and updates the displayed task table by
     * calling the {@code complectionTable} method. If the content is empty, it sets an error
     * message in the {@code notCorrectFieldFill} label.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void CreateTask(ActionEvent ignoredEvent) {
        String contentTask = task.getText().trim();
        if (!contentTask.isEmpty()) {
            stringPriority = (stringPriority == null) ? "Приоритет 4" : stringPriority;
            String stringDedline = (dedline.getValue() == null) ?
                    "Дедлайн не назначен" : dedline.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (isSameTask(contentTask, stringDedline, notCorrectFieldFill)) return;
            DATABASE.insertTask(username, contentTask, stringPriority, stringDedline, NOT_COMPLETED);
            complectionTable();
            resetParameters();
        } else {
            notCorrectFieldFill.setText("Поле содержания задачи не должно быть пустым!!!");
        }
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method clears the text in the {@code task} input field, resets the {@code priority}
     * field, and sets the {@code stringPriority} to its default value. It also clears the
     * deadline selection.
     */
    private void resetParameters() {
        task.setText("");
        priority.setText("");
        stringPriority = "Приоритет 4";
        dedline.setValue(null);
    }

    /**
     * This method iterates through the items in the table and compares each task's content,
     * priority, and deadline with the provided values. If a match is found, it sets the text
     * of the {@code notCorrectFieldFill} label to indicate that a duplicate task exists and
     * returns {@code true}. If no match is found, it returns {@code false}.
     *
     * @param contentTask         The content of the task to check for duplicates.
     * @param stringDedline       The deadline of the task to check for duplicates.
     * @param notCorrectFieldFill The label to display an error message if a duplicate is found.
     * @return {@code true} if a duplicate task exists, {@code false} otherwise.
     */
    private boolean isSameTask(String contentTask, String stringDedline, Label notCorrectFieldFill) {
        for (Task item : table.getItems()) {
            if (item.getContent().equals(contentTask) &&
                    item.getPriority().equals(stringPriority) &&
                    item.getDedline().equals(stringDedline)) {
                notCorrectFieldFill.setText("Точно такая же задача уже есть в списке!!!");
                return true;
            }
        }
        return false;
    }

    /**
     * This method retrieves the list of tasks for the current user from the database and
     * populates the table view with the task details. It sets up the cell value factories
     * for the columns to display the content, priority, deadline, and status of each task.
     */
    private void complectionTable() {
        table.getItems().clear();
        ObservableList<Task> tasks = DataProcessingFromAndForDatabase.makeListOfTableElements(username);
        tasksColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        prioritiesColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        dedlinesColumn.setCellValueFactory(new PropertyValueFactory<>("dedline"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.getItems().addAll(tasks);
    }

    /**
     * This method call the {@code clearErrorLabel} method
     *
     * @param ignoredEvent The key event that triggered this method (not used).
     */
    @FXML
    void clearLabel(KeyEvent ignoredEvent) {
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method call the {@code clearErrorLabel} method
     *
     * @param ignoredEvent The key event that triggered this method (not used).
     */
    @FXML
    void clearLabelUnderRename(KeyEvent ignoredEvent) {
        notCorrectFieldFill.setText("");
        notCorrectRenameFieldFill.setText("");
        taskNotSelected.setText("");
    }

    /**
     * This method call the {@code clearErrorLabel} method
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void exchangeDate(ActionEvent ignoredEvent) {
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method sets the text of the {@code notCorrectRenameFieldFill} and
     * {@code notCorrectFieldFill} labels to an empty string, effectively clearing any
     * previous error messages.
     */
    private void clearErrorLabelAndSelectedTask() {
        notCorrectFieldFill.setText("");
        notCorrectRenameFieldFill.setText("");
        selectedTask = null;
        taskNotSelected.setText("");
    }

    /**
     * This method deletes all tasks marked as completed from the database and
     * removes them from the table view. It also calls the method
     * {@code clearLabelsAndSelectedTask} to reset any labels and the selected task.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void removeAllCompletedTasks(ActionEvent ignoredEvent) {
        DATABASE.deleteAllExecuteTasks(username);
        table.getItems().removeIf(item -> item.getStatus().equals("выполнено"));
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method clears the current items in the table, executes all tasks in the
     * database, and updates the displayed table by calling the method
     * {@code complectionTable}. It also resets any labels and the selected task.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void executeAllTasks(ActionEvent ignoredEvent) {
        DATABASE.executeAllTasks(username);
        complectionTable();
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method deletes all tasks from the database and clears the items in the
     * table view. It also calls the method {@code clearLabelsAndSelectedTask}
     * to reset any labels and the selected task.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void removeAllTasks(ActionEvent ignoredEvent) {
        DATABASE.deleteAllTasks(username);
        table.getItems().clear();
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method attempts to create a new window for the login screen. If an {@code IOException}
     * occurs during the window creation, it throws a {@code RuntimeException}.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void exit(ActionEvent ignoredEvent) {
        try {
            createWindow();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method loads the FXML file for the login screen and sets it as the current scene
     * for the existing stage.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    void createWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "login/todoList-startScreen.fxml"));
        Stage stage = (Stage) buttonExit.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 501, 546));
        stage.show();
    }

    /**
     * This method is called when the user clicks on the pane. It updates the text on the execute button
     * by calling the method {@code writeTextOnButtonExecute}.
     *
     * @param ignoredEvent The mouse event that triggered this method (not used).
     */
    @FXML
    void clickOnPane(MouseEvent ignoredEvent) {
        writeTextOnButtonExecute();
    }

    /**
     * This method is called when the user clicks on a task in the table. It sets the {@code selectedTask}
     * to the currently selected item and updates the button text by calling the method
     * {@code writeTextOnButtonExecute}.
     *
     * @param ignoredEvent The mouse event that triggered this method (not used).
     */
    @FXML
    void getSelectedTask(MouseEvent ignoredEvent) {
        selectedTask = table.getSelectionModel().getSelectedItem();
        writeTextOnButtonExecute();
    }

    /**
     * This method checks if a task is selected. If no task is selected, it sets the button text
     * to "Задача не выполнена" (Task completed). If a task is selected and its status is {@code COMPLETED},
     * it sets the button text to "Задача выполнена" (Task not completed). Otherwise, it sets the text
     * to "Задача выполнена" (Task completed). It also clears any error messages related to task selection.
     */
    private void writeTextOnButtonExecute() {
        if (selectedTask == null) {
            buttonExecute.setText("Задача выполнена");
        } else if (selectedTask.getStatus().equals(COMPLETED)) {
            buttonExecute.setText("Задача не выполнена");
        } else {
            buttonExecute.setText("Задача выполнена");
        }
        taskNotSelected.setText("");
    }

    /**
     * This method first clears any error messages. It then checks if a task is selected. If no task is
     * selected, it displays an error message. If a task is selected, it calls the method {@code writeTextOnButtonExecuteViceVersa}
     * to update the button text. If the selected task's status is {@code NOT_COMPLETED}, it executes the task
     * in the database and updates the displayed table. If the task is already completed, it cancels the execution
     * of the task. Finally, it resets the selected task to {@code null}.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void executeTask(ActionEvent ignoredEvent) {
        if (selectedTask == null) {
            taskNotSelected.setText("Задача для действия не выбрана!!!");
            return;
        }
        writeTextOnButtonExecute();
        if (selectedTask.getStatus().equals(NOT_COMPLETED)) {
            DATABASE.executeTask(username, selectedTask);
            complectionTable();
        } else {
            DATABASE.canselExecutionTask(username, selectedTask);
            complectionTable();
        }
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method checks if a task is selected. If not, it displays an error message.
     * If a task is selected, it removes the task from the database and updates the table
     * to reflect the removal. Finally, it clears any labels and resets the selected task.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void removeTask(ActionEvent ignoredEvent) {
        if (selectedTask == null) {
            taskNotSelected.setText("Задача для действия не выбрана!!!");
            return;
        }
        DATABASE.deleteTask(username, selectedTask);
        table.getItems().removeIf(item -> item.getContent().equals(selectedTask.getContent()) &&
                item.getPriority().equals(selectedTask.getPriority()) &&
                item.getDedline().equals(selectedTask.getDedline()));
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method checks if a task is selected. If not, it displays an error message.
     * If a task is selected, it retrieves the content from the input field. If the new
     * content is not empty, it checks if the task is the same as an existing task.
     * If it is not the same, it renames the task in the database and updates the table.
     * If the content is empty, it displays an error message. Finally, it clears any labels
     * and resets the selected task.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void renameTask(ActionEvent ignoredEvent) {
        if (selectedTask == null) {
            taskNotSelected.setText("Задача для действия не выбрана!!!");
            return;
        }
        String contentTask = renameTask.getText().trim();
        if (!contentTask.isEmpty()) {
            stringPriority = selectedTask.getPriority();
            String stringDedline = selectedTask.getDedline();
            if (isSameTask(contentTask, stringDedline, notCorrectRenameFieldFill)) return;
            DATABASE.renameTask(username, contentTask, selectedTask);
            complectionTable();
            resetParameters();
        } else {
            notCorrectRenameFieldFill.setText("Поле содержания задачи не должно быть пустым!!!");
        }
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method is triggered by an action event and updates the {@code stringPriority}
     * variable to reflect the selected priority. It then calls the method to set the
     * priority label and clear any error messages.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void setPriority1(ActionEvent ignoredEvent) {
        stringPriority = "Приоритет 1";
        setPrioritiesAndClearLabels();
    }

    /**
     * This method is triggered by an action event and updates the {@code stringPriority}
     * variable to reflect the selected priority. It then calls the method to set the
     * priority label and clear any error messages.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void setPriority2(ActionEvent ignoredEvent) {
        stringPriority = "Приоритет 2";
        setPrioritiesAndClearLabels();
    }

    /**
     * This method is triggered by an action event and updates the {@code stringPriority}
     * variable to reflect the selected priority. It then calls the method to set the
     * priority label and clear any error messages.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void setPriority3(ActionEvent ignoredEvent) {
        stringPriority = "Приоритет 3";
        setPrioritiesAndClearLabels();
    }

    /**
     * This method is triggered by an action event and updates the {@code stringPriority}
     * variable to reflect the selected priority. It then calls the method to set the
     * priority label and clear any error messages.
     *
     * @param ignoredEvent The action event that triggered this method (not used).
     */
    @FXML
    void setPriority4(ActionEvent ignoredEvent) {
        stringPriority = "Приоритет 4";
        setPrioritiesAndClearLabels();
    }

    /**
     * This method sets the text of the {@code priority} label to the current value of
     * {@code stringPriority}. If the {@code notCorrectFieldFill} label contains a specific
     * error message, it is cleared. Additionally, the {@code notCorrectRenameFieldFill}
     * label is also cleared.
     */
    private void setPrioritiesAndClearLabels() {
        priority.setText(stringPriority);
        clearErrorLabelAndSelectedTask();
    }

    /**
     * This method sets the {@code username} variable and updates the greeting label
     * to welcome the user. It also calls the {@code complectionTable()} method to
     * populate the task table.
     *
     * @param username The username of the user to be welcomed.
     */
    @FXML
    public void initialize(String username) {
        this.username = username;
        greeting.setText("Добро пожаловать в приложение ToDoList, " + username + "!");
        complectionTable();
    }
}
