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
    @Constraints.MinLength (value = 5, message = "Title should be minimum 5 characters long.")
    @Constraints.MaxLength (value = 25, message = "Title should shorter. Write more details in description.")
    @Constraints.Required (message = "Title is required.")
    public String title;
    @Constraints.MinLength (value = 25, message = "Description should contain more details.")
    @Constraints.Required (message = "Description is required.")
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
        return finder.where().eq("user", user).findList();
    }

    public static Reservation findByUserAndPlace(User user, Place place) {
        return finder.where().eq("user", user).eq("place", place).findUnique();
    }

    public static List<Reservation> findByStatus(User user, Status status) {
        return finder.where().eq("place", user).where().eq("status", status).findList();
    }

}
