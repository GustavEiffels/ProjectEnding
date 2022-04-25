package com.make.plan.service.forCustomer.email;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailSenderService {
    public void sendMail(String title, String sendTo, String content) throws MessagingException {
        final String user = "|";
        final String password = "|";
        final String sender ="|";
        String smtpMailLocate = "smtp.naver.com";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", smtpMailLocate);
        prop.put("mail.smtp.port", 465);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo, sender, "UTF-8"));
            message.setSubject(title);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
