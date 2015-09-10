package models;

import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;
import play.data.validation.Constraints;

import play.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class representing Place model.
 */
@Entity
public class Place extends Model {

    public static Finder<Integer, Place> finder = new Finder<>(Integer.class, Place.class);
    // declaration of parameters
    @Id
    public Integer id;
    @Constraints.Required
    @Constraints.MaxLength(150)
    public String title;
    @Column(columnDefinition = "TEXT")
    public String description;
    public Double longitude;
    public Double latitude;
    public String address;
    public Calendar placeCreated;
    @ManyToOne
    public User user;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Service> services;

    /**
     * Default constructor.
     */
    public Place() {

    }

}