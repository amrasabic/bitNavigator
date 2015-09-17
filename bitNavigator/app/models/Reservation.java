package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

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

    public Reservation() {

    }

}
