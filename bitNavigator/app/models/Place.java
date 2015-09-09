package models;

import javax.persistence.*;

import play.db.ebean.*;
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
    @Constraints.Required
    @Column(columnDefinition = "TEXT")
    public String description;
    public Double longitude;
    public Double latitude;
    //@Constraints.Required
  //  public String phone;
    public Calendar placeCreated;
    @ManyToOne
    public User user;
//    @OneToMany
//    private static List<String> imageLists = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Place() {

    }

}