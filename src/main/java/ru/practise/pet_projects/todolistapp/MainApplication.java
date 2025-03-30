package ru.practise.pet_projects.todolistapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration_part/login/" +
                "todoList-startScreen.fxml"));
        primaryStage.setTitle("ToDoListApplication!");
        primaryStage.setScene(new Scene(fxmlLoader.load(), 501, 546));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}