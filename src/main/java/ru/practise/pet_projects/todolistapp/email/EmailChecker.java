package ru.practise.pet_projects.todolistapp.email;


import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;


/**
 * The {@code EmailChecker} class is responsible for sending email verification codes
 * to users for email confirmation in the ToDoList application.
 */
@Log4j2
@Getter
public class EmailChecker {
    private final String code;
    private final EmailService emailService;

    /**
     * Constructs an {@code EmailChecker} instance with random codes of verification email
     */
    public EmailChecker() {
        code = makeRandomCode();
        emailService = new EmailService();
    }

    /**
     * This method sets up the email session with authentication and sends the
     * verification code to the provided {@code email} address.
     *
     * @param email The recipient's email address to which the verification code will be sent.
     */
    public void sendCodeToCheck(String email) {
        Session session = emailService.settingSession();
        try {
            sendEmail(email, session);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * Creates an email containing the verification code.
     *
     * @param email   The recipient's email address.
     * @param session The email session used to send the message.
     * @throws MessagingException If there is an error in creating email.
     */
    private MimeMessage createEmail(String email, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailService.getEmailFrom()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Подтвердите email на ToDoList");
        String htmlContent = convertHTMLToString(code);
        message.setContent(htmlContent, "text/html; charset=utf-8");
        return message;
    }

    private void sendEmail(String email, Session session) throws MessagingException {
        MimeMessage message = createEmail(email, session);
        Transport.send(message);
    }

    /**
     * This method reads the content from an HTML file located at
     * "Content.html". It searches for the placeholder
     * "{{code}}" within the file content and replaces it with the specified code.
     *
     * @param code the code to be inserted into the HTML template. This code will replace the
     *             "{{code}}" placeholder in the HTML content.
     * @return a String representing the HTML content with the placeholder replaced by the provided code.
     * @throws RuntimeException if there is an error reading the file or if the file does not exist.
     */
    private String convertHTMLToString(String code) {
        try (InputStream fileInputStream = getClass().getResourceAsStream("Content.html")) {
            if (fileInputStream == null) {
                throw new IOException("Файл не найден: Content.html");
            }
            byte[] buffer = fileInputStream.readAllBytes();
            String fileContent = new String(buffer, StandardCharsets.UTF_8);
            return fileContent.replace("{{code}}", code);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a random verification code.
     *
     * @return A {@code String} representing a random verification code between 100000 and 999999.
     */
    private String makeRandomCode() {
        Random r = new Random();
        return String.valueOf(r.nextInt(100000, 1000000));
    }
}
