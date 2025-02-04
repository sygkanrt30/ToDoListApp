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
import ru.practise.pet_projects.todolistapp.MainApplication;
import ru.practise.pet_projects.todolistapp.handlers.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;


public class MainBodyController {
    private String stringPriority;
    private String username;
    private static final String COMPLETED = "выполнено";
    private static final String NOT_COMPLETED = "не выполнено";
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
        selectedTask = null;
        String contentTask = task.getText().trim();
        if (!contentTask.isEmpty()) {
            stringPriority = (stringPriority == null) ? "Приоритет 4" : stringPriority;
            String stringDedline = (dedline.getValue() == null) ? "Дедлайн не назначен" : dedline.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (isSameTask(contentTask, stringDedline, notCorrectFieldFill)) return;
            table.getItems().clear();
            DATABASE.insertTask(username, contentTask, stringPriority, stringDedline, NOT_COMPLETED);
            complectionTable();
            resetParameters();

        } else {
            notCorrectFieldFill.setText("Поле содержания задачи не должно быть пустым!!!");
        }
        notCorrectRenameFieldFill.setText("");
        taskNotSelected.setText("");
    }

    private void resetParameters() {
        task.setText("");
        priority.setText("");
        stringPriority = "Приоритет 4";
        dedline.setValue(null);
    }

    private boolean isSameTask(String contentTask, String stringDedline, Label notCorrectFieldFill) {
        for (Task item : table.getItems()) {
            if (item.getContent().equals(contentTask) && item.getPriority().equals(stringPriority) && item.getDedline().equals(stringDedline)) {
                notCorrectFieldFill.setText("Точно такая же задача уже есть в списке!!!");
                return true;
            }
        }
        return false;
    }

    private void complectionTable() {
        ObservableList<Task> tasks = DATABASE.getTasks(username);
        tasksColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        prioritiesColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        dedlinesColumn.setCellValueFactory(new PropertyValueFactory<>("dedline"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.getItems().addAll(tasks);
    }

    @FXML
    void clearLabel(KeyEvent ignoredEvent) {
        notCorrectRenameFieldFill.setText("");
        notCorrectFieldFill.setText("");
    }

    @FXML
    void clearLabelUnderRename(KeyEvent ignoredEvent) {
        notCorrectRenameFieldFill.setText("");
        notCorrectFieldFill.setText("");
    }

    @FXML
    void exchangeDate(ActionEvent ignoredEvent) {
        if (notCorrectFieldFill.getText().equals("Точно такая же задача уже есть в списке!!!")) {
            notCorrectFieldFill.setText("");
        }
        notCorrectRenameFieldFill.setText("");
    }

    @FXML
    void removeAllCompletedTasks(ActionEvent ignoredEvent) {
        DATABASE.deleteAllExecuteTasks(username);
        table.getItems().removeIf(item -> item.getStatus().equals("выполнено"));
        clearLabelsAndSelectedTask();
    }

    @FXML
    void executeAllTasks(ActionEvent ignoredEvent) {
        table.getItems().clear();
        DATABASE.executeAllTasks(username);
        complectionTable();
        clearLabelsAndSelectedTask();
    }

    @FXML
    void removeAllTasks(ActionEvent ignoredEvent) {
        DATABASE.deleteAllTasks(username);
        table.getItems().clear();
        clearLabelsAndSelectedTask();
    }

    private void clearLabelsAndSelectedTask() {
        selectedTask = null;
        notCorrectRenameFieldFill.setText("");
        notCorrectFieldFill.setText("");
        taskNotSelected.setText("");
    }

    @FXML
    void exit(ActionEvent ignoredEvent) {
        try {
            createWindow();
        } catch (IOException e) {
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

    private void writeTextOnButtonExecuteViceVersa() {
        if (selectedTask == null) {
            buttonExecute.setText("Задача выполнена");
        } else if (selectedTask.getStatus().equals(NOT_COMPLETED)) {
            buttonExecute.setText("Задача не выполнена");
        } else {
            buttonExecute.setText("Задача выполнена");
        }
        taskNotSelected.setText("");
    }

    @FXML
    void executeTask(ActionEvent ignoredEvent) {
        notCorrectRenameFieldFill.setText("");
        notCorrectFieldFill.setText("");
        if (selectedTask == null) {
            taskNotSelected.setText("Задача для действия не выбрана!!!");
            return;
        }
        writeTextOnButtonExecuteViceVersa();
        if (selectedTask.getStatus().equals(NOT_COMPLETED)) {
            table.getItems().clear();
            DATABASE.executeTask(username, selectedTask);
            complectionTable();
        } else {
            table.getItems().clear();
            DATABASE.canselExecutionTask(username, selectedTask);
            complectionTable();
        }
        selectedTask = null;
    }

    @FXML
    void removeTask(ActionEvent ignoredEvent) {
        if (selectedTask == null) {
            taskNotSelected.setText("Задача для действия не выбрана!!!");
            return;
        }
        DATABASE.removeTask(username, selectedTask);
        table.getItems().removeIf(item -> item.getContent().equals(selectedTask.getContent()) &&
                item.getPriority().equals(selectedTask.getPriority()) &&
                item.getDedline().equals(selectedTask.getDedline()));
        clearLabelsAndSelectedTask();
    }

    @FXML
    void renameTask(ActionEvent ignoredEvent) {
        if (selectedTask == null) {
            taskNotSelected.setText("Задача для действия не выбрана!!!");
            return;
        }
        String newContentTask = renameTask.getText().trim();
        if (!newContentTask.isEmpty()) {
            table.getItems().clear();
            stringPriority = selectedTask.getPriority();
            String stringDedline = selectedTask.getDedline();
            if (isSameTask(newContentTask, stringDedline, notCorrectRenameFieldFill)) return;
            table.getItems().clear();
            DATABASE.renameTask(username, newContentTask, selectedTask);
            complectionTable();
            renameTask.setText("");
            dedline.setValue(null);
        } else {
            notCorrectRenameFieldFill.setText("Поле содержания задачи не должно быть пустым!!!");
        }
        notCorrectFieldFill.setText("");
        selectedTask = null;
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
        stringPriority = "Приоритет 4";
        setPrioritiesAndClearLabels();
    }

    private void setPrioritiesAndClearLabels() {
        priority.setText(stringPriority);
        if (notCorrectFieldFill.getText().equals("Точно такая же задача уже есть в списке!!!")) {
            notCorrectFieldFill.setText("");
        }
        notCorrectRenameFieldFill.setText("");
    }

    @FXML
    public void initialize(String username) {
        this.username = username;
        greeting.setText("Добро пожаловать в приложение ToDoList, " + username + "!");
        complectionTable();
    }
}
