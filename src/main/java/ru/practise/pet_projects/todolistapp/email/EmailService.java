package ru.practise.pet_projects.todolistapp.email;

import lombok.Getter;
import ru.practise.pet_projects.todolistapp.utils.PropertyReader;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Getter
public class EmailService {
    private static final PropertyReader PROPERTIES = new PropertyReader("ToDoListApp.properties");
    private final String emailFrom = PROPERTIES.get("EMAIL_FROM");
    private final Properties settingOfEmail;

    public EmailService() {
        this.settingOfEmail = settingProperties();
    }

    private Properties settingProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", PROPERTIES.get("mail.smtp.auth"));
        properties.put("mail.smtp.ssl.enable", PROPERTIES.get("mail.smtp.ssl.enable"));
        properties.put("mail.smtp.host", PROPERTIES.get("mail.smtp.host"));
        properties.put("mail.smtp.port", PROPERTIES.get("mail.smtp.port"));
        return properties;
    }

    public Session settingSession() {
        Session session = Session.getInstance(settingOfEmail, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, PROPERTIES.get("EMAIL_PASSWORD"));
            }
        });
        session.setDebug(true);
        return session;
    }
}
