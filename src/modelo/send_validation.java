/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import com.sun.mail.smtp.SMTPTransport;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Yessica
 */
public class send_validation {

    private final String SMTP_SERVER = "smtp.gmail.com";
    private final String USERNAME = "gomezalex0921@gmail.com";
    private final String PASSWORD = "abc2021cine";
    private final String EMAIL_FROM = "gomezalex0921@gmail.com";


    public boolean sendMessage(database db, persona person, String text, String subject) {
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587");
        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);
        try {

            msg.setFrom(new InternetAddress(EMAIL_FROM));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(person.getCorreo_persona()));
            msg.setSubject(subject);
            msg.setText(text);
            SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
            transport.connect(SMTP_SERVER, USERNAME, PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
            String result = (transport.getLastServerResponse());
            transport.close();
            if (result.contains("OK")) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al enviar mensaje: {0}" + e);
            return false;
        }
        return false;
    }
}
