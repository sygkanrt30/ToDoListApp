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



@Log4j2
@Getter
public class EmailChecker {
    private final String code;
    private final EmailService emailService;

    public EmailChecker() {
        code = makeRandomCode();
        emailService = new EmailService();
    }

    public void sendCodeToCheck(String email) {
        Session session = emailService.settingSession();
        try {
            sendEmail(email, session);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

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

    private String makeRandomCode() {
        Random r = new Random();
        return String.valueOf(r.nextInt(100000, 1000000));
    }
}
