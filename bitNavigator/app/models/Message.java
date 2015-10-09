package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Calendar;
import java.util.List;

/**
 * Created by amra.sabic on 9/30/15.
 */
@Entity
public class Message extends Model{

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

    public Message() {

    }

    public static Finder<Integer, Message> finder = new Finder<>(Message.class);

    public static List<Message> findAll() {
        return finder.all();
    }

    public static List<Message> findByUser(User user) {
        return finder.where().eq("user", user).findList();
    }

    public static List<Message> findByReservation(Integer id) {
        Reservation reservation = Reservation.findById(id);
        return finder.where().eq("reservation", reservation).findList();
    }

}
