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
    // declaration of enums
    public Enum typeOfService {
        ACCOMMODATION, COFFEE, FOOD, SHOPING, NIGHTLIFE, SPORT, SERVICES, TRANSPORTATION
    }

    public enum workingDays {
        MONDAY, TUESDAY, WEDNESDAY,
        THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public static Finder<Integer, Place> finder = new Finder<>(Integer.class, Place.class);
    // declaration of parameters
    @Constraints.Required
    public String title;
    public String description;
    public Gallery gallery;
    @Constraints.Required
    public Enum typeOfService;
    public Enum workingDays;
    @Constraints.Required
    public String address;
    @Constraints.Required
    public BigInteger phone;
    public Calendar placeCreated;

    /**
     * Default constructor.
     */
    public Place() {

    }

    /**
     * Constructor
     * @param title
     * @param description
     * @param gallery
     * @param typeOfService
     * @param workingDays
     * @param address
     * @param phone
     */
    public Place(String title, String description, Gallery gallery, Enum typeOfService, Enum workingDays, String address, BigInteger phone) {
        this.title = title;
        this.description = description;
        this.gallery = gallery;
        this.typeOfService = typeOfService;
        this.workingDays = workingDays;
        this.address = address;
        this.phone = phone;
    }

}