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

/**
 * The {@code StartMenuController} class manages the user interface and logic for the start menu of the application.
 * It handles user interactions for account creation and user login, validates user credentials,
 * and navigates between different screens of the application.
 */
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

    /**
     * Navigates to the account creation screen when the create account button is pressed.
     *
     * @param ignoredEvent The ActionEvent triggered by the button press.
     */
    @FXML
    void CreateAccount(ActionEvent ignoredEvent) {
        try {
            createLogUpWindow();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates the entered email and password, and logs the user in if the credentials are correct.
     *
     * @param ignoredEvent The ActionEvent triggered by the button press.
     * @throws IOException If an input or output exception occurs while loading the next scene.
     */
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

    /**
     * Highlights the input fields for email and password when the user
     * enters incorrect credentials. It adds a highlight style to the
     * {@code emailField} and {@code passwordField}, and sets error
     * messages to indicate an invalid email address or password.
     */
    private void highlightingWrongInputToEnter() {
        emailField.getStyleClass().add("highlighted");
        passwordField.getStyleClass().add("highlighted");
        notCorrectPasswordOrEmail1.setText("\uD83D\uDEABНеверный адрес электронной почты или пароль!");
        notCorrectPasswordOrEmail2.setText("\uD83D\uDEABНеверный адрес электронной почты или пароль!");
    }

    /**
     * Creates and displays the account creation window.
     *
     * @throws IOException If an input or output exception occurs while loading the FXML file.
     */
    private void createLogUpWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/" +
                "logup/todoList-logUpScreen.fxml"));
        Stage stage = (Stage) buttonCreateAccount.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 511, 716));
        stage.show();
    }

    /**
     * Creates and displays the main application window after a successful login.
     *
     * @param user The User object representing the logged-in user.
     * @throws IOException If an input or output exception occurs while loading the FXML file.
     */
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

    /**
     * Clears error messages and highlighted styles when a key is pressed in the input fields.
     *
     * @param ignoredEvent The KeyEvent triggered by a key press.
     */
    @FXML
    void clearLabel(KeyEvent ignoredEvent) {
        notCorrectPasswordOrEmail1.setText("");
        notCorrectPasswordOrEmail2.setText("");
        emailField.getStyleClass().remove("highlighted");
        passwordField.getStyleClass().remove("highlighted");
    }
}