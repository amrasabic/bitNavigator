package models;

import com.avaje.ebean.Model;
import utillities.SessionHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by amra.sabic on 9/16/2015.
 */
@Entity
public class Reservation extends Model {

    @Id
    public Integer id;
    @ManyToOne
    public User user;
    @ManyToOne
    public Place place;
    @ManyToOne
    public Status status;
    public Calendar timestamp;
    public Calendar reservationDate;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Message> messages;

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

    public static List<Reservation> findByPlace(Place place) {
        return finder.where().eq("place", place).findList();
    }

    public static Reservation findByUserAndPlace(User user, Place place) {
        List<Reservation> reservations = finder.where().eq("user", user).eq("place", place).findList();
        for (Reservation reservation : reservations) {
            if(reservation.status.id == Status.APPROVED) {
                return reservation;
            }
        }
        if (reservations.size() > 0) {
            return finder.where().eq("user", user).eq("place", place).findList().get(0);
        }
        return null;
    }

    public static List<Reservation> getAllReservationsOnUsersPlaces(Status status) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (Place place : Place.findByUser(SessionHelper.getCurrentUser())) {
            for (Reservation reservation : Reservation.findByPlace(place)) {
                if (reservation.status.id == status.id) {
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    public static List<Reservation> getAllReservationsOnUsersPlaces() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (Place place : Place.findByUser(SessionHelper.getCurrentUser())) {
            reservations.addAll(Reservation.findByPlace(place));
        }
        return reservations;
    }

}
