package models;

import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.Model;
import controllers.MessageController;
import play.Logger;
import play.data.validation.Constraints;
import utillities.SessionHelper;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Calendar;
import java.util.List;

/**
 * Created by amra.sabic on 9/30/15.
 */

//This class represents Message model
@Entity
public class Message extends Model{

    //atributes
    @Id
    public Integer id;
    @Constraints.Required (message = "Can not send an empty message.")
    public String content;
    @ManyToOne (cascade = CascadeType.PERSIST)
    public Reservation reservation;
    public Calendar sent;
    @ManyToOne (cascade = CascadeType.PERSIST)
    public User sender;
    @ManyToOne (cascade = CascadeType.PERSIST)
    public User reciever;
    public boolean seen = false;

    public Message() {

    }

    //Finder for message
    public static Finder<Integer, Message> finder = new Finder<>(Message.class);

    //Finds all messages
    public static List<Message> findAll() {
        return finder.all();
    }


    //Finds Messages by reservation
    public static List<Message> findByReservation(Integer id) {
        Reservation reservation = Reservation.findById(id);
        return finder.where().eq("reservation", reservation).findList();

    }

    //Sets message as seen, notification wont show up any more
    public static void seen(Message message) {
        message.seen = true;
        message.update();
    }

    //Checks if message is new(if message is not seen)
    public static boolean isNewMessage(Message message) {
        if (message.reciever.equals(SessionHelper.getCurrentUser())) {
            return !message.seen;
        }
        return false;
    }

    //Gets new messages
    public static List<Message> getNewMessages(User reciever) {
        return finder.where().eq("reciever", reciever).eq("seen", 0).findList();
    }

    //Gets number of new messages, number of new messages is number of notifications user will have
    public static int numberOfNewMessages(Reservation reservation) {
        int counter = 0;
        for (Message message : Message.findByReservation(reservation.id)) {
            if (Message.isNewMessage(message)) {
                counter++;
            }
        }
        return counter;
    }

    //Getts number of new messages by list of reservations
    public static int numberOfNewMessages(List<Reservation> reservations) {
        int counter = 0;
        for (Reservation reservation : reservations) {
            counter += numberOfNewMessages(reservation);
        }
        return counter;
    }

    //Gets number of new messages by status
    public static int numberOfNewMessages(int type) throws IllegalArgumentException {
        switch (type) {
            case MessageController.ALL:
                return numberOfNewMessages(Reservation.getAllReservationsOnUsersPlaces());
            case MessageController.APPROVED:
                return numberOfNewMessages(Reservation.getAllReservationsOnUsersPlaces(Status.findById(Status.APPROVED)));
            case MessageController.WAITING:
                return numberOfNewMessages(Reservation.getAllReservationsOnUsersPlaces(Status.findById(Status.WAITING)));
            case MessageController.DENIED:
                return numberOfNewMessages(Reservation.getAllReservationsOnUsersPlaces(Status.findById(Status.DENIED)));
            case MessageController.ANSWERS:
                return numberOfNewMessages(Reservation.findByUser(SessionHelper.getCurrentUser()));
            default:
                throw new IllegalArgumentException();
        }
    }

    //Finds users due they are senders or receivers
    public static List<Message> findBySenderAndReciever(User user) {
        ExpressionFactory exprFactory = finder.getExpressionFactory();
        return finder.where().or(exprFactory.eq("sender", user), exprFactory.eq("reciever", user)).findList();
    }

}
