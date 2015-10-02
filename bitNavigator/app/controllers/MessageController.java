package controllers;

import models.Comment;
import models.Message;
import models.Reservation;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utillities.Authenticators;
import utillities.SessionHelper;
import views.html.messages.helper._inbox;
import views.html.messages.helper._messageslist;


import java.util.Calendar;
import java.util.List;

/**
 * Created by amra.sabic on 9/30/15.
 */
public class MessageController extends Controller {

    private static final Form<Message> messageForm = Form.form(Message.class);

    @Security.Authenticated(Authenticators.User.class)
    public Result messagesList(Integer reservationId) {
        List<Message> messages = Message.findByReservation(reservationId);
        return ok(_messageslist.render(messages));
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result sendMessage(Integer reservationId){
        List<Message> messages = Message.findByReservation(reservationId);
        Form<Message> boundForm = messageForm.bindFromRequest();
        Message message = new Message();

        String content = boundForm.bindFromRequest().field("content").value();
        if(content == null){
            return badRequest();
        }
        message.content = content;

        User user = SessionHelper.getCurrentUser();
        message.sender = user;
        Reservation reservation = Reservation.findById(reservationId);
        message.reservation = reservation;

        message.messageCreated = Calendar.getInstance();
        message.save();
        messages.add(message);

        return ok(_messageslist.render(messages));
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result inbox(){
        User user = SessionHelper.getCurrentUser();
        List<Reservation> reservations = Reservation.findByUser(user);
        return ok(_inbox.render(reservations));
    }

}
