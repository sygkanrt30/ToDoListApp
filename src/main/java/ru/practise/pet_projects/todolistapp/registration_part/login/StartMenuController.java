package ru.practise.pet_projects.todolistapp.registration_part.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.practise.pet_projects.todolistapp.MainApplication;
import ru.practise.pet_projects.todolistapp.user.User;
import ru.practise.pet_projects.todolistapp.database.DatabaseInteraction;

import java.io.IOException;


public class StartMenuController {
    public static final DatabaseInteraction DATABASE = new DatabaseInteraction();

    @FXML
    private Button buttonCreateAccount;

    @FXML
    private Button buttonEnter;

    @FXML
    private TextField emailField;

    @FXML
    private Label notCorrectPasswordOrEmail1;

    @FXML
    private Label notCorrectPasswordOrEmail2;

    @FXML
    private PasswordField passwordField;

    @FXML
    void CreateAccount(ActionEvent event) {
        try {
            createWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void createWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/logup/todoList-logUpScreen.fxml"));
        Stage stage = (Stage) buttonCreateAccount.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 511, 716));
        stage.show();
    }

    @FXML
    void Enter(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        User user = DATABASE.getUsersInfo(email);
        if (user != null && password.equals(user.getPassword())) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main_part/todoList-MainBody.fxml"));
            Stage stage = (Stage) buttonEnter.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load(), 860, 559));
            stage.show();
        } else {
            emailField.getStyleClass().add("highlighted");
            passwordField.getStyleClass().add("highlighted");
            notCorrectPasswordOrEmail1.setText("\uD83D\uDEABНеверный адрес электронной почты или пароль!");
            notCorrectPasswordOrEmail2.setText("\uD83D\uDEABНеверный адрес электронной почты или пароль!");
        }

    }
}