package uk.gov.gds.dm;

import play.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSupport {
    public static void sendMail(String msgBody) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("martyn.inglis@digital.cabinet-office.gov.uk"));
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
