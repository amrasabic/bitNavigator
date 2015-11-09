package models;

import com.avaje.ebean.Model;
import play.Logger;
import utillities.SessionHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by amra.sabic on 9/16/2015.
 */
//Reservation model
@Entity
public class Reservation extends Model {

    //Atributes
    @Id
    public Integer id;
    @ManyToOne (cascade = CascadeType.PERSIST)
    public User user;
    @ManyToOne (cascade = CascadeType.PERSIST)
    public Place place;
    @ManyToOne (cascade = CascadeType.PERSIST)
    public Status status;
    public Calendar timestamp;
    public Calendar reservationDate;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Message> messages;
    @Column
    public Double price;
    public String paymentId;

    //Finder for reservations
    public static Finder<Integer, Reservation> finder = new Finder<>(Reservation.class);

    public Reservation() {

    }

    //Finds reservations by id
    public static Reservation findById(int id) {
        return finder.byId(id);
    }

    //Finds reservations by place owner
     public static List<Reservation> findByPlaceOwner(User user) {
        return finder.where().eq("place.user", user).findList();
     }

    //Finds reservation by payment id
    public static Reservation findByPaymentId(String id){
        return finder.where().eq("paymentId",id).findUnique();
    }

    //Finds all resevations
    public static List<Reservation> findAll() {
        return finder.all();
    }


    //Finds reservations user
    public static List<Reservation> findByUser(User user) {
        return finder.where().eq("user", user).findList();
    }

    //Finds reservations by place
    public static List<Reservation> findByPlace(Place place) {
        return finder.where().eq("place", place).findList();
    }

    //Finds reservations by place and user
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

    /**
     * Finds all reservations on users place by status
     * @param status status of reservation(can be Approved, Wating or Denied
     * @return list of reservations
     */
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

    /**
     * Finds all reservations on current users place (gets user from session)
     * @return list of reservation
     */
    public static List<Reservation> getAllReservationsOnUsersPlaces() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (Place place : Place.findByUser(SessionHelper.getCurrentUser())) {
            reservations.addAll(Reservation.findByPlace(place));
        }
        return reservations;
    }

    /**
     * Finds all reservations on users place
     * @param u User (finds user by place)
     * @return list of reservations
     */
    public static List<Reservation> getAllReservationsOnUserPlaces(User u) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (Place place : Place.findByUser(u)) {
            reservations.addAll(Reservation.findByPlace(place));
        }
        return reservations;
    }

}
