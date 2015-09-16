package models;

import com.avaje.ebean.Model;

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
    public String title;
    public String description;
    public Status status;

    public Reservation() {

    }

}
