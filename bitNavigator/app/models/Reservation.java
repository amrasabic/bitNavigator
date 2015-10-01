package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Amra on 9/16/2015.
 */
@Entity
public class Reservation extends Model {

    @Id
    public Integer id;
    @ManyToOne
    public User user;
    @ManyToOne
    public Place place;
    @Constraints.MinLength (value = 5, message = "Title should be minimum 5 characters long.")
    @Constraints.MaxLength (value = 25, message = "Title should shorter. Write more details in description.")
    @Constraints.Required (message = "Title is required.")
    public String title;
    @Constraints.MinLength (value = 25, message = "Description should contain more details.")
    @Constraints.Required (message = "Description is required.")
    public String description;
    @ManyToOne
    public Status status;
    public Calendar reservationCreated;
    public String reservationDay;

    public static Finder<Integer, Reservation> finder = new Finder<>(Reservation.class);

    public Reservation() {

    }

    public static Reservation findById(int id) {
        return finder.byId(id);
    }

    public static List<Reservation> findAll() {
        return finder.all();
    }

    public static List<Reservation> findByUser(User user) {
        return finder.where().eq("user", user).findList();
    }

    public static Reservation findByUserAndPlace(User user, Place place) {
        List<Reservation> reservations = finder.where().eq("user", user).eq("place", place).findList();
        for (Reservation reservation : reservations) {
            if(reservation.status.id == Status.APPROVED) {
                return reservation;
            }
        }
        if (reservations.size() > 0) {
            return finder.where().eq("user", user).eq("place", place).findList().get(0);
        }
        return null;
    }

    public static List<Reservation> findByStatus(User user, Status status) {
        return finder.where().eq("place", user).where().eq("status", status).findList();
    }

}
