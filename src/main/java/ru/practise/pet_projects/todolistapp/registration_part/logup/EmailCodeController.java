package ru.practise.pet_projects.todolistapp.registration_part.logup;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import ru.practise.pet_projects.todolistapp.MainApplication;
import ru.practise.pet_projects.todolistapp.email.EmailChecker;
import ru.practise.pet_projects.todolistapp.main_part.MainBodyController;

import java.io.IOException;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;


@Log4j2
public class EmailCodeController {
    private int k = 4;
    private final EmailChecker emailChecker = new EmailChecker();
    private String email;
    private String password;
    private String username;

    @FXML
    private Button buttonBack, buttonContinue, buttonSendCode;

    @FXML
    private TextField codeField;

    @FXML
    private Label notCorrectCode, countTries;

    @FXML
    void BackToLogUpScreen(ActionEvent ignoredEvent) {
        try {
            createLogUpWindow();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @FXML
    void Continue(ActionEvent ignoredEvent) throws IOException {
        if (emailChecker.getCode().equals(codeField.getText().trim())) {
            DATABASE.insertUser(email, password, username);
            createMainWindow();
        } else {
            codeField.getStyleClass().add("highlighted");
            notCorrectCode.setText("\uD83D\uDEAB Неверный код подтверждения!");
        }
    }

    private void createLogUpWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "logup/todoList-logUpScreen.fxml"));
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 511, 716));
        stage.show();
    }

    private void createMainWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main_part/" +
                "todoList-mainPart.fxml"));
        Parent parent = fxmlLoader.load();
        MainBodyController mainBodyController = fxmlLoader.getController();
        mainBodyController.initialize(username);
        Stage stage = (Stage) buttonContinue.getScene().getWindow();
        stage.setScene(new Scene(parent, 945, 726));
        stage.show();
    }

    @FXML
    void sendCode(ActionEvent ignoredEvent) {
        if (k > 0) {
            emailChecker.sendCodeToCheck(email);
            k--;
            countTries.setText("Осталось попыток: " + k);
        } else {
            k *= 0;
        }
    }

    @FXML
    public void initialize(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        emailChecker.sendCodeToCheck(email);
    }

    @FXML
    void clearLabel(KeyEvent ignoredEvent) {
        notCorrectCode.setText("");
        codeField.getStyleClass().remove("highlighted");
    }
}
