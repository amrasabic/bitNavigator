package controllers.rest.controllers;

import models.Message;
import models.Place;
import models.Reservation;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by hajrudin.sehic on 06/11/15.
 */
public class ReservationRestController extends Controller {

    private List<ReservationJSON> reservations = new ArrayList<>();

    /**
     * This method find all reservations from our database on user places
     *
     * @return list of reservations in json format
     */
    public Result getListOfReservationsOnMyPlaces() {
        User u = getUserFromRequest();
        for (Reservation r : Reservation.getAllReservationsOnUserPlaces(u)) {
            reservations.add(new ReservationJSON(r));
        }
        return ok(Json.toJson(reservations));
    }

    /**
     * This method find all user reservations drom our database
     *
     * @return list of user reservations in json format
     */
    public Result getListOfUserReservations() {
        User u = getUserFromRequest();
        for (Reservation r : Reservation.findByUser(u)) {
            reservations.add(new ReservationJSON(r));
        }
        return ok(Json.toJson(reservations));
    }

    private User getUserFromRequest() {
        DynamicForm form = Form.form().bindFromRequest();
        String user_email = form.data().get("user_email");
        return User.findByEmail(user_email);
    }

    /**
     * This method cheks user inputs for reservation from android app request
     * and if everything is valid save it to database
     * @return ok request if reservation is saved to database else returns badRequest
     */
    public Result reservationSubmit() {
        DynamicForm form = Form.form().bindFromRequest();
        String placeId = form.data().get("place_id");
        String userEmail = form.data().get("user_email");
        String day = form.data().get("day");
        String time = form.data().get("time");
        String message = form.data().get("message");
        SimpleDateFormat myDate = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        Calendar date = new GregorianCalendar();
        try {
            date.setTime(myDate.parse(day + " " + time));
        } catch (ParseException e) {
            return badRequest();
        }
        Place p = Place.findById(Integer.parseInt(placeId));
        User u = User.findByEmail(userEmail);
        Reservation r = new Reservation();
        r.place = p;
        r.user = u;
        Message m = new Message();
        m.sender = u;
        if (u.equals(r.place.user)) {
            m.reciever = r.user;
        } else {
            m.reciever = r.place.user;
        }
        m.content = message;
        r.timestamp = Calendar.getInstance();
        r.reservationDate = date;
        r.messages.add(m);
        r.status = models.Status.findById(models.Status.WAITING);
        r.save();
        m.sent = Calendar.getInstance();
        m.reservation.id = r.id;
        m.save();
        if (p.numOfReservations == null) {
            p.numOfReservations = 0;
        }
        p.numOfReservations++;
        p.update();
        return ok();
    }

    private class ReservationJSON {

        public Integer id;
        public String place_title;
        public String status;
        public String date;

        public ReservationJSON(Reservation r) {
            this.id = r.id;
            this.place_title = r.place.title;
            this.status = r.status.status;
            this.date = r.reservationDate.getTime().toString();
        }
    }

}
