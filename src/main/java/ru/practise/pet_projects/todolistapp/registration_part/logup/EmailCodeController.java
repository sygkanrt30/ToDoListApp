package ru.practise.pet_projects.todolistapp.registration_part.logup;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.practise.pet_projects.todolistapp.MainApplication;
import ru.practise.pet_projects.todolistapp.emailCode.EmailChecker;

import java.io.IOException;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;


public class EmailCodeController {
    private int k = 6;
    private final EmailChecker emailChecker = new EmailChecker();
    private String email;
    private String password;
    private String username;

    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonContinue;

    @FXML
    private Button buttonSendCode;

    @FXML
    private TextField codeField;

    @FXML
    private Label notCorrectCode;

    @FXML
    private Label countTries;


    @FXML
    void BackToLogUpScreen(ActionEvent event) {
        try {
            createWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void createWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/logup/todoList-logUpScreen.fxml"));
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 511, 716));
        stage.show();
    }

    @FXML
    void Continue(ActionEvent event) throws IOException {
        if (emailChecker.getCode().equals(codeField.getText().trim())) {
            DATABASE.insertUser(email, password, username);
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("src/main/resources/ru/practise/pet_projects/" +
                    "todolistapp/main_part/todoList-MainBody.fxml"));
            Stage stage = (Stage) buttonContinue.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load(), 860, 559));
            stage.show();
        } else {
            codeField.getStyleClass().add("highlighted");
            notCorrectCode.setText("\uD83D\uDEAB Неверный код подтверждения!");
        }
    }

    @FXML
    void sendCode(ActionEvent event) {
        if (k == 6) {
            emailChecker.sendCheckCodeTo(email);
            buttonSendCode.setText("Отправить код повторно");
            buttonSendCode.setPrefWidth(192);
            k--;
            countTries.setText("Осталось попыток: " + k);
        } else if (k > 0) {
            emailChecker.sendCheckCodeTo(email);
            k--;
            countTries.setText("Осталось попыток: " + k);
        } else {
            k *= 0;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
