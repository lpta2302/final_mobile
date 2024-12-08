package com.dev.mail.lpta2302.final_mobile.logic.mail;

import android.os.Handler;
import android.os.Looper;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {
    private MailService() {}
    public static MailService getInstance() {
        return new MailService();
    }

    public void sendEmail(Mail mail, SendMailCallback callback) {
        new Thread(() -> {
            try {
                final String toEmail = mail.getTo();
                final String subject = mail.getSubject();
                final String content = mail.getContent();
                final String fromEmail = "pictoblog0912@gmail.com"; // Your email address
                final String password = "shvk ncii tfie rlde "; // Your email's app password

                // Set up the mail server properties
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // Create the session
                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

                // Compose the email
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(subject);
                message.setText(content);

                // Send the email
                Transport.send(message);
                new Handler(Looper.getMainLooper()).post(callback::onSuccess);
            }
            catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        }).start();
    }
}