package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import utillities.SessionHelper;

import javax.persistence.*;
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
    @Column
    public Double price;
    public String paymentId;

    public static Finder<Integer, Reservation> finder = new Finder<>(Reservation.class);

    public Reservation() {

    }

    public static Reservation findById(int id) {
        return finder.byId(id);
    }

    public static Reservation findByPaymentId(String id){
        return finder.where().eq("paymentId",id).findUnique();
    }

    public static List<Reservation> findAll() {
        return finder.all();
    }

    public static List<Reservation> findByUser(User user) {
        return finder.where().eq("user", user).findList();
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

    public static List<Reservation> findByStatus(User user, Status status) {
        return finder.where().eq("place", user).where().eq("status", status).findList();
    }

    public static Integer reservationsOnWaiting(){
        List<Reservation> reservations = Reservation.findByStatus(SessionHelper.getCurrentUser(), Status.findById(Status.WAITING));
        return reservations.size();
    }

    public static Integer findByUser(){
        List<Reservation> reservations = Reservation.findByUser(SessionHelper.getCurrentUser());
        return reservations.size();
    }

    public static Integer findByStatusInt(Integer status) {
        List<Reservation> reservations = Reservation.findByStatus(SessionHelper.getCurrentUser(), Status.findById(status));
        return reservations.size();
    }

    public static List<Reservation> findByReservationAndStatus(Integer id, Integer status) {
        Reservation reservation = Reservation.findById(id);
        return finder.where().eq("reservation", reservation).eq("status", status).findList();
    }
}
