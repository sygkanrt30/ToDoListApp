package ru.practise.pet_projects.todolistapp.registration_part.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import ru.practise.pet_projects.todolistapp.MainApplication;
import ru.practise.pet_projects.todolistapp.database.DatabaseInteraction;
import ru.practise.pet_projects.todolistapp.database.SqlQueries;
import ru.practise.pet_projects.todolistapp.database.UserInteraction;
import ru.practise.pet_projects.todolistapp.handlers.User;
import ru.practise.pet_projects.todolistapp.main_part.MainBodyController;

import java.io.IOException;


@Log4j2
public class StartMenuController {
    public static final DatabaseInteraction DATABASE = new DatabaseInteraction();
    private static final UserInteraction USER_INTERACTION = new UserInteraction();

    @FXML
    private Button buttonCreateAccount, buttonEnter;

    @FXML
    private TextField emailField;

    @FXML
    private Label notCorrectPasswordOrEmail1, notCorrectPasswordOrEmail2;

    @FXML
    private PasswordField passwordField;

    @FXML
    void CreateAccount(ActionEvent ignoredEvent) {
        try {
            createLogUpWindow();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @FXML
    void Enter(ActionEvent ignoredEvent) throws IOException {
        String password = passwordField.getText();
        User user = USER_INTERACTION.getUserInfo(SqlQueries.getUserByLoginQuery(), emailField.getText());
        if (user != null && password.equals(user.password())) {
            createMainWindow(user);
        } else {
            highlightingWrongInputToEnter();
        }
    }

    private void highlightingWrongInputToEnter() {
        emailField.getStyleClass().add("highlighted");
        passwordField.getStyleClass().add("highlighted");
        notCorrectPasswordOrEmail1.setText("\uD83D\uDEABНеверный адрес электронной почты или пароль!");
        notCorrectPasswordOrEmail2.setText("\uD83D\uDEABНеверный адрес электронной почты или пароль!");
    }

    private void createLogUpWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "logup/todoList-logUpScreen.fxml"));
        Stage stage = (Stage) buttonCreateAccount.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 511, 716));
        stage.show();
    }

    private void createMainWindow(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main_part/" +
                "todoList-mainPart.fxml"));
        Parent parent = fxmlLoader.load();
        MainBodyController mainBodyController = fxmlLoader.getController();
        mainBodyController.initialize(user.username());
        Stage stage = (Stage) buttonEnter.getScene().getWindow();
        stage.setScene(new Scene(parent, 945, 726));
        stage.show();
    }

    @FXML
    void clearLabel(KeyEvent ignoredEvent) {
        notCorrectPasswordOrEmail1.setText("");
        notCorrectPasswordOrEmail2.setText("");
        emailField.getStyleClass().remove("highlighted");
        passwordField.getStyleClass().remove("highlighted");
    }
}