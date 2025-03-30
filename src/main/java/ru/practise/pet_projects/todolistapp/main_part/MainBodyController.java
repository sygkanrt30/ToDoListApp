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
import lombok.extern.log4j.Log4j2;
import ru.practise.pet_projects.todolistapp.MainApplication;
import ru.practise.pet_projects.todolistapp.database.TaskInteraction;
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
@Log4j2
public class MainBodyController {
    private static final TaskInteraction TASK_INTERACTION = new TaskInteraction();
    private String stringPriority;
    private String username;
    private static final String COMPLETED = "выполнено";
    private static final String NOT_COMPLETED = "не выполнено";
    public static final String DEFAULT_PRIORITY = "Приоритет 4";
    private Task selectedTask;

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


    @FXML
    void CreateTask(ActionEvent ignoredEvent) {
        String contentTask = task.getText().trim();
        if (!contentTask.isEmpty()) {
            stringPriority = (stringPriority == null) ? DEFAULT_PRIORITY : stringPriority;
            String stringDedline = (dedline.getValue() == null) ?
                    "Дедлайн не назначен" : dedline.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (isSameTask(contentTask, stringDedline, notCorrectFieldFill)) {
                return;
            }
            DATABASE.insertTask(username, contentTask, stringPriority, stringDedline, NOT_COMPLETED);
            complectionTable();
            resetParameters();
        } else {
            notCorrectFieldFill.setText("Поле содержания задачи не должно быть пустым!!!");
        }
        clearErrorLabelAndSelectedTask();
    }

    private void resetParameters() {
        task.setText("");
        priority.setText("");
        stringPriority = DEFAULT_PRIORITY;
        dedline.setValue(null);
    }

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

    private void complectionTable() {
        table.getItems().clear();
        ObservableList<Task> tasks = TASK_INTERACTION.getTasks(username);
        tasksColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        prioritiesColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        dedlinesColumn.setCellValueFactory(new PropertyValueFactory<>("dedline"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.getItems().addAll(tasks);
    }

    @FXML
    void clearLabel(KeyEvent ignoredEvent) {
        clearErrorLabelAndSelectedTask();
    }

    @FXML
    void clearLabelUnderRename(KeyEvent ignoredEvent) {
        notCorrectFieldFill.setText("");
        notCorrectRenameFieldFill.setText("");
        taskNotSelected.setText("");
    }

    @FXML
    void exchangeDate(ActionEvent ignoredEvent) {
        clearErrorLabelAndSelectedTask();
    }

    private void clearErrorLabelAndSelectedTask() {
        notCorrectFieldFill.setText("");
        notCorrectRenameFieldFill.setText("");
        selectedTask = null;
        taskNotSelected.setText("");
    }

    @FXML
    void removeAllCompletedTasks(ActionEvent ignoredEvent) {
        DATABASE.deleteAllExecuteTasks(username);
        table.getItems().removeIf(item -> item.getStatus().equals(COMPLETED));
        clearErrorLabelAndSelectedTask();
    }

    @FXML
    void executeAllTasks(ActionEvent ignoredEvent) {
        DATABASE.executeAllTasks(username);
        complectionTable();
        clearErrorLabelAndSelectedTask();
    }

    @FXML
    void removeAllTasks(ActionEvent ignoredEvent) {
        DATABASE.deleteAllTasks(username);
        table.getItems().clear();
        clearErrorLabelAndSelectedTask();
    }

    @FXML
    void exit(ActionEvent ignoredEvent) {
        try {
            createWindow();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    void createWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "login/todoList-startScreen.fxml"));
        Stage stage = (Stage) buttonExit.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 501, 546));
        stage.show();
    }

    @FXML
    void clickOnPane(MouseEvent ignoredEvent) {
        writeTextOnButtonExecute();
    }

    @FXML
    void getSelectedTask(MouseEvent ignoredEvent) {
        selectedTask = table.getSelectionModel().getSelectedItem();
        writeTextOnButtonExecute();
    }

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

    @FXML
    void setPriority1(ActionEvent ignoredEvent) {
        stringPriority = "Приоритет 1";
        setPrioritiesAndClearLabels();
    }

    @FXML
    void setPriority2(ActionEvent ignoredEvent) {
        stringPriority = "Приоритет 2";
        setPrioritiesAndClearLabels();
    }

    @FXML
    void setPriority3(ActionEvent ignoredEvent) {
        stringPriority = "Приоритет 3";
        setPrioritiesAndClearLabels();
    }

    @FXML
    void setPriority4(ActionEvent ignoredEvent) {
        stringPriority = DEFAULT_PRIORITY;
        setPrioritiesAndClearLabels();
    }

    private void setPrioritiesAndClearLabels() {
        priority.setText(stringPriority);
        clearErrorLabelAndSelectedTask();
    }

    @FXML
    public void initialize(String username) {
        this.username = username;
        greeting.setText("Добро пожаловать в приложение ToDoList, " + username + "!");
        complectionTable();
    }
}
