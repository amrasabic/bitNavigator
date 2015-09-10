package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by ognjen.cetkovic on 10/09/15.
 */
@Entity
public class Comment extends Model {

    @Id
    public int id;
    @Constraints.Required
    public String commentContent;
    public Calendar commentCreated;
    @ManyToOne
    public Place place;
    @ManyToOne
    public User user;

    public static Finder<Integer, Comment> finder = new Finder<Integer, Comment>(Integer.class, Comment.class);



}
