package uk.gov.gds.dm;

import play.Logger;
import play.mvc.Controller;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class GAEActions extends Controller {

    public static void login() {
        String url = params.get("continue");
        render(url);
    }

    public static void doLogin(String email, String url, boolean isAdmin) {
        if (email != null && !email.trim().equals("")) {
            session.put("__GAE_EMAIL", email);
            session.put("__GAE_ISADMIN", isAdmin);
        }
        redirect(url);
    }

    public static void logout() {
        String url = params.get("continue");
        session.clear();
        redirect(url);
    }

    public static void sendMail(String msgBody) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("suppliers@digitalmarketplace.service.gov.uk"));
            msg.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress("suppliers@digitalmarketplace.service.gov.uk", "Support")
            );
            msg.setSubject("Submission Error");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.info("Email failed to send");
        }

    }

}