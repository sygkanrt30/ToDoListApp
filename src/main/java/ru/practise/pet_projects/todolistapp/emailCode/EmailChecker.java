package ru.practise.pet_projects.todolistapp.emailCode;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailChecker {
    private final String from;
    private final String host;
    private final String port;
    private final String code;

    public EmailChecker() {
        from = "todolistapplication252@gmail.com";
        host = "smtp.gmail.com";
        port = "465";
        code = makeRandomCode();
    }

    public String getCode() {
        return code;
    }

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

    private Properties settingProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        return properties;
    }

    private void creatingAndSendingEmail(String email, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Подтвердите email на ToDoList");
        String htmlContent = "<html><body style='font-family:Arial;'>"
                + "<h1 style='font-family: Verdana, Geneva, sans-serif; font-size: 24px; font-style: normal; font-variant: normal;" +
                " font-weight: 700; line-height: 26.4px;'>Ваш код для подтверждения email в приложение ToDoList:</h1>"
                + "<p style='font-family: Verdana, Geneva, sans-serif; font-size: 24px; font-style: " +
                "normal; font-variant: normal; font-weight: 700; line-height: 26.4px;'>" + code + "</p>"
                + "</body></html>";
        message.setContent(htmlContent, "text/html; charset=utf-8");
        Transport.send(message);
    }

    private String makeRandomCode() {
        Random r = new Random();
        return String.valueOf(r.nextInt(100000, 1000000) + 1);
    }
}
