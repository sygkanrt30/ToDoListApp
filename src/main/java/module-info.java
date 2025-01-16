module ru.practise.pet_projects.todolistapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;


    opens ru.practise.pet_projects.todolistapp to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp;
    opens ru.practise.pet_projects.todolistapp.registration_part to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp.registration_part.logup;
    opens ru.practise.pet_projects.todolistapp.registration_part.logup to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp.database;
    exports ru.practise.pet_projects.todolistapp.user;
    opens ru.practise.pet_projects.todolistapp.user to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp.registration_part.login;
    opens ru.practise.pet_projects.todolistapp.registration_part.login to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp.emailCode;
    exports ru.practise.pet_projects.todolistapp.main_part;
    opens ru.practise.pet_projects.todolistapp.main_part to javafx.fxml;
}