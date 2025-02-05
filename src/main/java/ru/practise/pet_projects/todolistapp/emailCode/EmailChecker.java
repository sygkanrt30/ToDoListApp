package ru.practise.pet_projects.todolistapp.emailCode;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;


/**
 * The {@code EmailChecker} class is responsible for sending email verification codes
 * to users for email confirmation in the ToDoList application.
 */
public class EmailChecker {
    private final String from;
    private final String host;
    private final String port;
    private final String code;

    /**
     * Constructs an {@code EmailChecker} instance with predefined email settings.
     * The {@code from} address is set to a specific Gmail account, and the SMTP
     * server settings are configured for Gmail.
     */
    public EmailChecker() {
        from = "todolistapplication252@gmail.com";
        host = "smtp.gmail.com";
        port = "465";
        code = makeRandomCode();
    }

    /**
     * Retrieves the generated verification {@code code}.
     *
     * @return A {@code String} representing the verification code.
     */
    public String getCode() {
        return code;
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
                        return new PasswordAuthentication(from, "idvpbbqibkfawmxl");
                    }
                });
        session.setDebug(true);
        try {
            creatingAndSendingEmail(email, session);
        } catch (Exception e) {
            e.printStackTrace();
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
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        return properties;
    }

    /**
     * Creates and sends an email containing the verification code.
     *
     * @param email The recipient's email address.
     * @param session The email session used to send the message.
     * @throws MessagingException If there is an error in creating or sending the email.
     */
    private void creatingAndSendingEmail(String email, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Подтвердите email на ToDoList");
        String htmlContent = "<html><body style='font-family:Arial;'>"
                + "<h1 style='font-family: Verdana, Geneva, sans-serif; font-size: 24px; font-style: normal; font-variant: normal;"
                + " font-weight: 700; line-height: 26.4px;'>Ваш код для подтверждения email в приложение ToDoList:</h1>"
                + "<p style='font-family: Verdana, Geneva, sans-serif; font-size: 24px; font-style: "
                + "normal; font-variant: normal; font-weight: 700; line-height: 26.4px;'>" + code + "</p>"
                + "</body></html>";
        message.setContent(htmlContent, "text/html; charset=utf-8");
        Transport.send(message);
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
