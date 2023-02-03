package registerUniqueDev.mailservice;

import javafx.util.converter.LocalDateStringConverter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;

public class JavaMailService {
    public static void sendMail(String toEmail, String header, String messageT) {
//            String to = toEmail;// change accordingly
        // Get the session object
        String from = "ianwanjala4@gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "radiyfhwjrsimoxv");// Put your email
                // id and
                // password here
            }
        });
        session.setDebug(true);
        // compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));// change accordingly
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(header);
            message.setSentDate(new Date());
            message.setText("your OTP is: " + messageT);
            // send message
            Transport.send(message);
            System.out.println("message sent successfully");
        }

        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}