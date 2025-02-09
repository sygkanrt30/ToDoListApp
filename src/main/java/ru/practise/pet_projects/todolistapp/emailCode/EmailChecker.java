package ru.practise.pet_projects.todolistapp.emailCode;


import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;


/**
 * The {@code EmailChecker} class is responsible for sending email verification codes
 * to users for email confirmation in the ToDoList application.
 */

@Getter
public class EmailChecker {
    public static final Logger LOGGER = LogManager.getLogger(EmailChecker.class);
    private static final String FROM = "todolistapplication252@gmail.com";
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "465";
    private final String code;

    /**
     * Constructs an {@code EmailChecker} instance with random codes of verification email
     */
    public EmailChecker() {
        code = makeRandomCode();
    }

    /**
     * This method sets up the email session with authentication and sends the
     * verification code to the provided {@code email} address.
     *
     * @param email The recipient's email address to which the verification code will be sent.
     */
    public void sendCodeToCheck(String email) {
        Properties properties = settingProperties();
        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM, "idvpbbqibkfawmxl");
                    }
                });
        session.setDebug(true);
        try {
            creatingAndSendingEmail(email, session);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Configures the properties for the email session.
     *
     * @return A {@code Properties} object containing the SMTP settings.
     */
    private Properties settingProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);
        return properties;
    }

    /**
     * Creates and sends an email containing the verification code.
     *
     * @param email   The recipient's email address.
     * @param session The email session used to send the message.
     * @throws MessagingException If there is an error in creating or sending the email.
     */
    private void creatingAndSendingEmail(String email, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Подтвердите email на ToDoList");
        String htmlContent = makeHTMLString(code);
        message.setContent(htmlContent, "text/html; charset=utf-8");
        Transport.send(message);
    }

    /**
     * This method reads the content from an HTML file located at
     * "emailCode/Content.html". It searches for the placeholder
     * "{{code}}" within the file content and replaces it with the specified code.
     *
     * @param code the code to be inserted into the HTML template. This code will replace the
     *             "{{code}}" placeholder in the HTML content.
     * @return a String representing the HTML content with the placeholder replaced by the provided code.
     * @throws RuntimeException if there is an error reading the file or if the file does not exist.
     */
    private String makeHTMLString(String code) {
        String fileContent;
        try (FileInputStream fileInputStream = new FileInputStream("Content.html")) {
            byte[] buffer = fileInputStream.readAllBytes();
            fileContent = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        fileContent = fileContent.replace("{{code}}", code);
        return fileContent;
    }

    /**
     * Generates a random verification code.
     *
     * @return A {@code String} representing a random verification code between 100000 and 999999.
     */
    private String makeRandomCode() {
        Random r = new Random();
        return String.valueOf(r.nextInt(100000, 1000000) + 1);
    }
}
