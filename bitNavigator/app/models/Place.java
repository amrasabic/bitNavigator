package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * Class representing Place model.
 */
@Entity
public class Place extends Model {

    // declaration of parameters
    @Id
    public Integer id;
    @Constraints.Required(message = "This field is required!")
    @Constraints.MaxLength(value = 150, message = "Title can not hold more than 150 characters!")
    public String title;
    @Column(columnDefinition = "TEXT")
    public String description;
    public Double longitude;
    public Double latitude;
    public String address;
    public Calendar placeCreated;
    @ManyToOne
    public User user;
    @ManyToOne
    public Service service;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Image> images;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Comment> comments;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Reservation> reservations;

    public static Finder<Integer, Place> finder = new Finder<>(Place.class);
    /**
     * Default constructor.
     */
    public Place() {

    }

    public static List<Place> findAll() {
        return finder.all();
    }

    public static Place findById(int id) {
        return finder.byId(id);
    }

    public static Place findByTitle(String title) {
        return finder.where().eq("title", title).findUnique();
    }

    public static List<Place> findByValue(String value) {
        return finder.where().contains("title", value).findList();
    }

}