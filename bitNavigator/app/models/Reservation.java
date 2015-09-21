package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
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
    @Constraints.Required
    public String title;
    @Constraints.Required
    public String description;
    @ManyToOne
    public Status status;

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
        return finder.where().eq("place.user", user).findList();
    }

    public static List<Reservation> findByStatus(User user, Status status) {
        return finder.where().eq("place", user).where().eq("status", status).findList();
    }

}
