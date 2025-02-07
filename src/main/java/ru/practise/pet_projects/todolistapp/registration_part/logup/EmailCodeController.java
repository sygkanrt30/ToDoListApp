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
import ru.practise.pet_projects.todolistapp.database.DatabaseInteraction;
import ru.practise.pet_projects.todolistapp.emailCode.EmailChecker;
import ru.practise.pet_projects.todolistapp.main_part.MainBodyController;

import java.io.IOException;

import static ru.practise.pet_projects.todolistapp.registration_part.login.StartMenuController.DATABASE;


/**
 * The {@code EmailCodeController} class manages the user interface and logic for the email
 * confirmation code entry during the registration process in the ToDoList application.
 * It handles user interactions, validates the confirmation code, and navigates between
 * different screens of the application.
 */
public class EmailCodeController {
    public static final Logger LOGGER = LogManager.getLogger(EmailCodeController.class);
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

    /**
     * Navigates back to the registration screen when the back button is pressed.
     *
     * @param ignoredEvent The ActionEvent triggered by the button press.
     */
    @FXML
    void BackToLogUpScreen(ActionEvent ignoredEvent) {
        try {
            createLogUpWindow();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates the entered confirmation code and proceeds to the main application
     * if the code is correct. Otherwise, it displays an error message.
     *
     * @param ignoredEvent The ActionEvent triggered by the button press.
     * @throws IOException If an input or output exception occurs while loading the next scene.
     */
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

    /**
     * Creates and displays the registration window.
     *
     * @throws IOException If an input or output exception occurs while loading the FXML file.
     */
    private void createLogUpWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "logup/todoList-logUpScreen.fxml"));
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 511, 716));
        stage.show();
    }

    /**
     * Creates and displays the main application window after successful registration.
     *
     * @throws IOException If an input or output exception occurs while loading the FXML file.
     */
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

    /**
     * Sends the confirmation code to the user's email and decrements the remaining attempts.
     *
     * @param ignoredEvent The ActionEvent triggered by the button press.
     */
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

    /**
     * Initializes the controller with user credentials and sends a verification code to the provided {@code email}.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @param username the user's username
     */
    @FXML
    public void initialize(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        emailChecker.sendCodeToCheck(email);
    }

    /**
     * Clears the error label and resets the styling of the code input field when a key event occurs.
     *
     * @param ignoredEvent tThe ActionEvent triggered by the button press.
     */
    @FXML
    void clearLabel(KeyEvent ignoredEvent) {
        notCorrectCode.setText("");
        codeField.getStyleClass().remove("highlighted");
    }
}
