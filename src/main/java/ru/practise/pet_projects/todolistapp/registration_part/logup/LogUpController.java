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
import ru.practise.pet_projects.todolistapp.database.UserInteraction;

import java.io.IOException;


@Log4j2
public class LogUpController {
    public static final String REGEX_USERNAME = "^[A-Za-zА-Яа-яЁё][A-Za-zА-Яа-яЁё0-9_]*$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$";
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*[a-zA-Z].*$";
    private static final UserInteraction USER_INTERACTION = new UserInteraction();

    @FXML
    private Button buttonBack, buttonContinue;

    @FXML
    private TextField email, password, username;

    @FXML
    private Label notCorrectPasswordOrEmail1, notCorrectPasswordOrEmail2, notCorrectUsername;

    @FXML
    void BackToStartScreen(ActionEvent ignoredEvent) {
        try {
            createWindow();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @FXML
    void Continue(ActionEvent ignoredEvent) throws IOException {
        boolean goToNextWindow = false;
        String stringEmail = email.getText();
        String stringUsername = username.getText();
        String stringPassword = password.getText();
        goToNextWindow = isGoToNextWindow(stringUsername, stringEmail, stringPassword, goToNextWindow);
        if (goToNextWindow) {
            createEmailCodeCheckerWindow(stringEmail, stringPassword, stringUsername);
        }
    }

    private boolean isGoToNextWindow(String stringUsername, String stringEmail, String stringPassword, boolean goToNextWindow) {
        if (isUsernameCorrect(stringUsername) && isEmailCorrect(stringEmail) && isPasswordCorrect(stringPassword)) {
            if (USER_INTERACTION.loginIsBusy(stringEmail) && USER_INTERACTION.usernameIsBusy(stringUsername)) {
                goToNextWindow = true;
            } else if (!USER_INTERACTION.loginIsBusy(stringEmail)) {
                email.getStyleClass().add("highlighted");
                notCorrectPasswordOrEmail1.setText("\uD83D\uDEABЭтот адрес электронной почты уже зарегистрирован в приложение!");
            } else if (!USER_INTERACTION.usernameIsBusy(stringUsername)) {
                username.getStyleClass().add("highlighted");
                notCorrectUsername.setText("\uD83D\uDEABЭто имя пользователя занято!");
            } else {
                email.getStyleClass().add("highlighted");
                username.getStyleClass().add("highlighted");
                notCorrectPasswordOrEmail1.setText("\uD83D\uDEABЭтот адрес электронной почты уже зарегистрирован в приложение!");
                notCorrectUsername.setText("\uD83D\uDEABЭто имя пользователя занято!");
            }
        } else if (!isUsernameCorrect(stringUsername)) {
            username.getStyleClass().add("highlighted");
            notCorrectUsername.setText("\uD83D\uDEABНекорректное имя пользователя!");
        } else {
            email.getStyleClass().add("highlighted");
            password.getStyleClass().add("highlighted");
            notCorrectPasswordOrEmail1.setText("\uD83D\uDEABНекорректный адрес электронной почты или пароль!");
            notCorrectPasswordOrEmail2.setText("\uD83D\uDEABНекорректный адрес электронной почты или пароль!");
        }
        return goToNextWindow;
    }

    private void createWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "login/todoList-startScreen.fxml"));
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 501, 546));
        stage.show();
    }

    private void createEmailCodeCheckerWindow(String stringEmail, String stringPassword, String stringUsername)
            throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "logup/todoList-emailCodeScreen.fxml"));
        Parent parent = fxmlLoader.load();
        EmailCodeController emailCodeController = fxmlLoader.getController();
        emailCodeController.initialize(stringEmail, stringPassword, stringUsername);
        Stage stage = (Stage) buttonContinue.getScene().getWindow();
        stage.setScene(new Scene(parent, 501, 442));
        stage.show();
    }

    private boolean isUsernameCorrect(String username) {
        if (username.length() > 2 && username.length() < 16) {
            return username.matches(REGEX_USERNAME);
        }
        return false;
    }

    private boolean isEmailCorrect(String email) {
        return email.matches(REGEX_EMAIL);
    }

    private boolean isPasswordCorrect(String password) {
        if (password.length() > 7 && password.length() < 17) {
            return password.matches(REGEX_PASSWORD);
        }
        return false;
    }

    @FXML
    void clearLabel(KeyEvent ignoredEvent) {
        notCorrectPasswordOrEmail1.setText("");
        notCorrectPasswordOrEmail2.setText("");
        email.getStyleClass().remove("highlighted");
        password.getStyleClass().remove("highlighted");
    }

    @FXML
    void clearLabelUsername(KeyEvent ignoredEvent) {
        notCorrectUsername.setText("");
        username.getStyleClass().remove("highlighted");
    }
}

