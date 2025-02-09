module ru.practise.pet_project.java.todolistapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.practise.pet_project.java.todolistapp to javafx.fxml;
    exports ru.practise.pet_project.java.todolistapp;
}