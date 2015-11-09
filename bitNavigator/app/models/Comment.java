package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ognjen.cetkovic on 10/09/15.
 */

//Comment model
@Entity
public class Comment extends Model {

    //Atributes
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

    //Finder for comment
    public static Finder<Integer, Comment> finder = new Finder<Integer, Comment>(Comment.class);

    //Returns  all comments
    public static List<Comment> findAll() {
        return finder.all();
    }

    //Finds comments by place
    public static List<Comment> findByPlace(Place place) {
        return finder.where().eq("place", place).findList();
    }

    //Finds comments by id
    public static Comment findById(int id) {
        return finder.byId(id);
    }

    //Finds comments by user and place
    public static Comment findByUserAndPlace(String email, Place place) {
        return finder.where().eq("user", User.findByEmail(email)).eq("place", place).findUnique();
    }

    //Finds comments by user
    public static List<Comment> findByUser(User user) {
        return finder.where().eq("user", user).findList();
    }

    //toString method for comment
    @Override
    public String toString() {
        return "(" + id + ") Comment: " + commentContent + " (" + rate + ")";
    }
}
