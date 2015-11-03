package controllers;

import models.*;
import play.Logger;
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

    public static final int ALL = 0;
    public static final int APPROVED = 1;
    public static final int WAITING = 2;
    public static final int DENIED = 3;
    public static final int ANSWERS = 4;

    private static final Form<Message> messageForm = Form.form(Message.class);

    @Security.Authenticated(Authenticators.User.class)
    public Result messagesList(Integer reservationId) {
        List<Message> messages = Message.findByReservation(reservationId);
        return ok(_messageslist.render(messages));
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result sendMessage(Integer reservationId){
        Reservation reservation = Reservation.findById(reservationId);
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
        if (user.equals(reservation.place.user)) {
            message.reciever = reservation.user;
        } else {
            message.reciever = reservation.place.user;
        }

        message.reservation = reservation;

        message.sent = Calendar.getInstance();
        message.save();
        messages.add(message);

        return ok(_messageslist.render(messages));
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result inbox(int type){
        switch (type) {
            case ALL:
                Logger.debug("'0'0'0:" + Reservation.getAllReservationsOnUsersPlaces().size());
                return ok(_inbox.render(Reservation.getAllReservationsOnUsersPlaces()));
            case APPROVED:
                return ok(_inbox.render(Reservation.getAllReservationsOnUsersPlaces(models.Status.findById(models.Status.APPROVED))));
            case WAITING:
                return ok(_inbox.render(Reservation.getAllReservationsOnUsersPlaces(models.Status.findById(models.Status.WAITING))));
            case DENIED:
                return ok(_inbox.render(Reservation.getAllReservationsOnUsersPlaces(models.Status.findById(models.Status.DENIED))));
            case ANSWERS:
                return ok(_inbox.render(Reservation.findByUser(SessionHelper.getCurrentUser())));
            default:
                return badRequest();
        }
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result validateForm() {
        Form<Message> boundForm = messageForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            return badRequest(boundForm.errorsAsJson());
        }
        return ok();
    }

}
