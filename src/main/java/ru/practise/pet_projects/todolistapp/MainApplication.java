package ru.practise.pet_projects.todolistapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The {@code MainApplication} class is the entry point of the ToDoList application.
 * It extends the JavaFX Application class and is responsible for initializing
 * and displaying the main user interface of the application.
 */
public class MainApplication extends Application {

    /**
     * The start method is called when the JavaFX application is launched.
     * It sets up the primary stage with the initial scene loaded from the
     * specified FXML file.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set.
     * @throws IOException If an input or output exception occurs while loading
     *                     the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/login/" +
                "todoList-startScreen.fxml"));
        primaryStage.setTitle("ToDoListApplication!");
        primaryStage.setScene(new Scene(fxmlLoader.load(), 501, 546));
        primaryStage.show();
    }

    /**
     * The main method serves as the entry point for the Java application.
     * It invokes the launch method to start the JavaFX application lifecycle.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch();
    }
}