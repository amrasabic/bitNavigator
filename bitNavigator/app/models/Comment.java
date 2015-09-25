package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ognjen.cetkovic on 10/09/15.
 */
@Entity
public class Comment extends Model {

    @Id
    public int id;
    @Constraints.Required
    public String commentContent;
    public Integer rate;
    public Calendar commentCreated;
    @ManyToOne
    public Place place;
    @ManyToOne
    public User user;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Report> reports;

    public static Finder<Integer, Comment> finder = new Finder<Integer, Comment>(Comment.class);

    public static List<Comment> findAll() {
        return finder.all();
    }

    public static List<Comment> findByPlace(Place place) {
        return finder.where().eq("place", place).findList();
    }

    public static Comment findById(int id) {
        return finder.byId(id);
    }

    public static Comment findByUserAndPlace(String email, Place place) {
        return finder.where().eq("user", User.findByEmail(email)).eq("place", place).findUnique();
    }

    public static List<Comment> findByUser(User user) {
        return finder.where().eq("user", user).findList();
    }

    @Override
    public String toString() {
        return "(" + id + ") Comment: " + commentContent + " (" + rate + ")";
    }
}
