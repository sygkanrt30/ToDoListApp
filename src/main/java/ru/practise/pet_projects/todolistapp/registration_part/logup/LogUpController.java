package ru.practise.pet_projects.todolistapp.registration_part.logup;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.practise.pet_projects.todolistapp.MainApplication;

import java.io.IOException;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;

public class LogUpController {
    public static final String REGEX_USERNAME = "^[A-Za-zА-Яа-яЁё][A-Za-zА-Яа-яЁё0-9_]*$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$";
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*[a-zA-Z].*$";

    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonContinue;

    @FXML
    private TextField email;

    @FXML
    private Label notCorrectPasswordOrEmail1;

    @FXML
    private Label notCorrectPasswordOrEmail2;

    @FXML
    private Label notCorrectUsername;

    @FXML
    private TextField password;

    @FXML
    private TextField username;


    @FXML
    void BackToStartScreen(ActionEvent event) {
        try {
            createWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void createWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/login/todoList-startScreen.fxml"));
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 501, 546));
        stage.show();
    }

    @FXML
    void Continue(ActionEvent event) throws IOException {
        String stringEmail = email.getText();
        String stringUsername = username.getText();
        String stringPassword = password.getText();
        if (usernameIsCorrect(stringUsername) && emailIsCorrect(stringEmail) && passwordIsCorrect(stringPassword)) {
            if (DATABASE.loginIsBusy(stringEmail)) {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/logup/todoList-emailCodeScreen.fxml"));
                Parent parent = fxmlLoader.load();
                EmailCodeController emailCodeController = fxmlLoader.getController();
                emailCodeController.setEmail(stringEmail);
                emailCodeController.setUsername(stringUsername);
                emailCodeController.setPassword(stringPassword);
                Stage stage = (Stage) buttonContinue.getScene().getWindow();
                stage.setScene(new Scene(parent, 501, 442));
                stage.show();
            } else {
                email.getStyleClass().add("highlighted");
                notCorrectPasswordOrEmail1.setText("\uD83D\uDEABЭтот адрес электронной почты уже зарегистрирован в приложение!");
            }
        } else if (!usernameIsCorrect(stringUsername)) {
            username.getStyleClass().add("highlighted");
            notCorrectUsername.setText("\uD83D\uDEABНекорректное имя пользователя!");
        } else {
            email.getStyleClass().add("highlighted");
            password.getStyleClass().add("highlighted");
            notCorrectPasswordOrEmail1.setText("\uD83D\uDEABНекорректный адрес электронной почты или пароль!");
            notCorrectPasswordOrEmail2.setText("\uD83D\uDEABНекорректный адрес электронной почты или пароль!");
        }
    }

    boolean usernameIsCorrect(String username) {
        if (username.length() > 2 && username.length() < 16) {
            return username.matches(REGEX_USERNAME);
        }
        return false;
    }

    boolean emailIsCorrect(String email) {
        return email.matches(REGEX_EMAIL);
    }

    boolean passwordIsCorrect(String password) {
        if (password.length() > 7 && password.length() < 17) {
            return password.matches(REGEX_PASSWORD);
        }
        return false;
    }

    String getEmail() {
        return email.getText();
    }
}

