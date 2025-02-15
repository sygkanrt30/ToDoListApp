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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.practise.pet_projects.todolistapp.MainApplication;

import java.io.IOException;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;

/**
 * The {@code LogUpController} class handles the user interface and logic for user registration.
 * It validates user input for username, email, and password, and manages navigation
 * between different screens in the application.
 */
public class LogUpController {
    public static final Logger LOGGER = LogManager.getLogger(LogUpController.class);
    public static final String REGEX_USERNAME = "^[A-Za-zА-Яа-яЁё][A-Za-zА-Яа-яЁё0-9_]*$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$";
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*[a-zA-Z].*$";

    @FXML
    private Button buttonBack, buttonContinue;

    @FXML
    private TextField email, password, username;

    @FXML
    private Label notCorrectPasswordOrEmail1, notCorrectPasswordOrEmail2, notCorrectUsername;

    /**
     * Navigates back to the start screen when the back button is pressed.
     *
     * @param ignoredEvent The ActionEvent triggered by the button press.
     */
    @FXML
    void BackToStartScreen(ActionEvent ignoredEvent) {
        try {
            createWindow();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates user input for {@code email}, {@code username}, and {@code password}. If valid, proceeds to the email code checker window.
     *
     * @param ignoredEvent The ActionEvent triggered by the continue button press.
     * @throws IOException If an input or output exception occurs while loading the next scene.
     */
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

    /**
     * Determines whether to proceed to the next window based on the validity
     * of the provided username, email, and password. This method validates
     * the inputs and checks against the database for existing users.
     * Displays error messages if any input is incorrect or if the email or username is already taken.
     *
     * @param stringUsername the username entered by the user
     * @param stringEmail    the email address entered by the user
     * @param stringPassword the password entered by the user
     * @param goToNextWindow a boolean indicating whether to proceed to the next window
     * @return {@code true} if valid credentials are provided and the user can
     * proceed to the next window; {@code false} otherwise
     */
    private boolean isGoToNextWindow(String stringUsername, String stringEmail, String stringPassword, boolean goToNextWindow) {
        if (isUsernameCorrect(stringUsername) && isEmailCorrect(stringEmail) && isPasswordCorrect(stringPassword)) {
            if (DATABASE.loginIsBusy(stringEmail) && DATABASE.usernameIsBusy(stringUsername)) {
                goToNextWindow = true;
            } else if (!DATABASE.loginIsBusy(stringEmail)) {
                email.getStyleClass().add("highlighted");
                notCorrectPasswordOrEmail1.setText("\uD83D\uDEABЭтот адрес электронной почты уже зарегистрирован в приложение!");
            } else if (!DATABASE.usernameIsBusy(stringUsername)) {
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

    /**
     * Creates and displays the start screen window.
     *
     * @throws IOException If an input or output exception occurs while loading the FXML file.
     */
    private void createWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "login/todoList-startScreen.fxml"));
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 501, 546));
        stage.show();
    }

    /**
     * This method loads the FXML layout for the email code screen, initializes the controller
     * with the provided {@code email}, {@code username}, and {@code password}, and then sets the scene of the current stage
     * to the newly loaded layout.
     *
     * @param stringEmail    The email address to be used in the email code screen.
     * @param stringPassword The password associated with the user.
     * @param stringUsername The username of the user.
     * @throws IOException If there is an error loading the FXML file.
     */
    private void createEmailCodeCheckerWindow(String stringEmail, String stringPassword, String stringUsername) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "logup/todoList-emailCodeScreen.fxml"));
        Parent parent = fxmlLoader.load();
        EmailCodeController emailCodeController = fxmlLoader.getController();
        emailCodeController.initialize(stringEmail, stringPassword, stringUsername);
        Stage stage = (Stage) buttonContinue.getScene().getWindow();
        stage.setScene(new Scene(parent, 501, 442));
        stage.show();
    }

    /**
     * Validates the provided {@code username}.
     * The username is considered valid if its length is greater than 2 and less than 16,
     * and it matches the defined regular expression for usernames.
     *
     * @param username The username to validate.
     * @return true if the username is valid, false otherwise.
     */
    private boolean isUsernameCorrect(String username) {
        if (username.length() > 2 && username.length() < 16) {
            return username.matches(REGEX_USERNAME);
        }
        return false;
    }

    /**
     * Validates the provided {@code email}.
     * The email is considered valid if it matches the defined regular expression for emails.
     *
     * @param email The email address to validate.
     * @return true if the email is valid, false otherwise.
     */
    private boolean isEmailCorrect(String email) {
        return email.matches(REGEX_EMAIL);
    }

    /**
     * Validates the provided {@code password}.
     * The password is considered valid if its length is greater than 7 and less than 17,
     * and it matches the defined regular expression for passwords.
     *
     * @param password The password to validate.
     * @return true if the password is valid, false otherwise.
     */
    private boolean isPasswordCorrect(String password) {
        if (password.length() > 7 && password.length() < 17) {
            return password.matches(REGEX_PASSWORD);
        }
        return false;
    }

    /**
     * This method is triggered by a key event and resets the text of the error labels
     * and removes the highlighted style from the email and password input fields.
     *
     * @param ignoredEvent The key event that triggered this method (ignored).
     */
    @FXML
    void clearLabel(KeyEvent ignoredEvent) {
        notCorrectPasswordOrEmail1.setText("");
        notCorrectPasswordOrEmail2.setText("");
        email.getStyleClass().remove("highlighted");
        password.getStyleClass().remove("highlighted");
    }

    /**
     * This method is triggered by a key event and resets the text of the error label
     * and removes the highlighted style from the username input field.
     *
     * @param ignoredEvent The key event that triggered this method (ignored).
     */
    @FXML
    void clearLabelUsername(KeyEvent ignoredEvent) {
        notCorrectUsername.setText("");
        username.getStyleClass().remove("highlighted");
    }
}

