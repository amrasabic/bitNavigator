package utillities;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import play.Logger;
import play.Play;

import static play.mvc.Controller.flash;


/**
 * Created by Semir on 5.10.2015.
 */
public class MailHelper {

    public static void send(String email, String message) {

        try {
            HtmlEmail mail = new HtmlEmail();
            mail.setSubject("bitNavigator Welcome");
            mail.setFrom("bitNavigator <" + Play.application().configuration().reference().getString("EMAIL_USERNAME_ENV") + ">");
            mail.addTo("Contact <" + Play.application().configuration().reference().getString("EMAIL_USERNAME_ENV") + ">");
            mail.addTo(email);
            mail.setMsg(message);
            mail.setHtmlMsg(String
                    .format("<html><body><strong> %s </strong> <p> %s </p> <p> %s </p> </body></html>",
                            "Thanks for signing up to bitNavigator!",
                            "Please confirm your Email adress .", message));
            mail.setHostName("smtp.gmail.com");
            mail.setStartTLSEnabled(true);
            mail.setSSLOnConnect(true);
            mail.setAuthenticator(new DefaultAuthenticator(
                    Play.application().configuration().reference().getString("EMAIL_USERNAME_ENV"),
                    Play.application().configuration().reference().getString("EMAIL_PASSWORD_ENV")
            ));
            mail.send();
        } catch (Exception e) {
            Logger.warn("Email error: " + e);
        }
    }

    public static void sendContactMessage(String name, String email, String message) {
        if (message != null) {

            try {
                HtmlEmail mail = new HtmlEmail();
                mail.setSubject("bitNavigator Contact Us");
                mail.setFrom("bitNavigator <" + Play.application().configuration().reference().getString("EMAIL_USERNAME_ENV") + ">");
                mail.addTo("Contact <" + Play.application().configuration().reference().getString("EMAIL_USERNAME_ENV") + ">");
                mail.addTo(email);
                mail.setMsg(message);
                mail.setHtmlMsg(String
                        .format("<html><body><strong> %s </strong>: <h4>%s:</h4><p> %s </p> </body></html>",
                                email, name, message));
                mail.setHostName("smtp.gmail.com");
                mail.setStartTLSEnabled(true);
                mail.setSSLOnConnect(true);
                mail.setAuthenticator(new DefaultAuthenticator(
                        Play.application().configuration().reference().getString("EMAIL_USERNAME_ENV"),
                        Play.application().configuration().reference().getString("EMAIL_PASSWORD_ENV")
                ));
                mail.send();
            } catch (Exception e) {
                flash("error", "Could not send verification mail");
                e.printStackTrace();
            }

        }
    }

    public static void sendForPassword(String email, String message) {

        try {
            HtmlEmail mail = new HtmlEmail();
            mail.setSubject("bitNavigator Have you forgot your password ?");
            mail.setFrom("bitNavigator <" + Play.application().configuration().reference().getString("EMAIL_USERNAME_ENV") + ">");
            mail.addTo("Contact <" + Play.application().configuration().reference().getString("EMAIL_USERNAME_ENV") + ">");
            mail.addTo(email);
            mail.setMsg(message);
            mail.setHtmlMsg(String
                    .format("<html><body><strong> %s </strong> <p> %s </p> <p> %s </p> </body></html>",
                            "You have forgot your password ?",
                            "Click on the following link to set new password :", message));
            mail.setHostName("smtp.gmail.com");
            mail.setStartTLSEnabled(true);
            mail.setSSLOnConnect(true);
            mail.setAuthenticator(new DefaultAuthenticator(
                    Play.application().configuration().reference().getString("EMAIL_USERNAME_ENV"),
                    Play.application().configuration().reference().getString("EMAIL_PASSWORD_ENV")
            ));
            mail.send();
        } catch (Exception e) {
            Logger.warn("Email error: " + e);
        }
    }

}
