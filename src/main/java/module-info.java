/**
 * The module `ru.practise.pet_projects.todolistapp` represents a To-Do List application.
 * This application utilizes JavaFX for its user interface, JavaMail for email functionality,
 * and Java SQL for database interactions.
 *
 * <p>This module requires the following libraries:</p>
 * <ul>
 *   <li><code>javafx.controls</code> - for JavaFX control components.</li>
 *   <li><code>javafx.fxml</code> - for using FXML files to define the user interface.</li>
 *   <li><code>java.sql</code> - for database connectivity and SQL operations.</li>
 *   <li><code>java.mail</code> - for sending emails.</li>
 * </ul>
 *
 * <p>The following packages are exported and opened for reflection:</p>
 * <ul>
 *   <li><code>ru.practise.pet_projects.todolistapp</code> - the main application package.</li>
 *   <li><code>ru.practise.pet_projects.todolistapp.registration_part.logup</code> - handles user registration logic.</li>
 *   <li><code>ru.practise.pet_projects.todolistapp.database</code> - contains database-related classes and methods.</li>
 *   <li><code>ru.practise.pet_projects.todolistapp.handlers</code> - contains event handlers for the application.</li>
 *   <li><code>ru.practise.pet_projects.todolistapp.registration_part.login</code> - manages user login functionality.</li>
 *   <li><code>ru.practise.pet_projects.todolistapp.email</code> - handles email code verification.</li>
 *   <li><code>ru.practise.pet_projects.todolistapp.main_part</code> - contains the main parts of the application.</li>
 * </ul>
 *
 * <p>Packages are opened to <code>javafx.fxml</code> to allow for runtime reflection, which is necessary for
 * loading FXML files during the application execution.</p>
 */
module ru.practise.pet_projects.todolistapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires static lombok;


    opens ru.practise.pet_projects.todolistapp to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp;
    opens ru.practise.pet_projects.todolistapp.registration_part to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp.registration_part.logup;
    opens ru.practise.pet_projects.todolistapp.registration_part.logup to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp.database;
    exports ru.practise.pet_projects.todolistapp.handlers;
    opens ru.practise.pet_projects.todolistapp.handlers to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp.registration_part.login;
    opens ru.practise.pet_projects.todolistapp.registration_part.login to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp.email;
    exports ru.practise.pet_projects.todolistapp.main_part;
    opens ru.practise.pet_projects.todolistapp.main_part to javafx.fxml;
    exports ru.practise.pet_projects.todolistapp.utils;
    opens ru.practise.pet_projects.todolistapp.utils to javafx.fxml;
}