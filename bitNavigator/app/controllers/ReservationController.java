package controllers;

import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import models.Status;

import views.html.place.*;
import views.html.reservations.reservationlist;

import play.Logger;

/**
 * Created by Amra on 9/17/2015.
 */
public class ReservationController extends Controller {

    private static final Form<Reservation> reservationForm = Form.form(Reservation.class);

    public Result submitReservation(int id){

        DynamicForm boundForm = Form.form().bindFromRequest();
        if (boundForm.hasErrors()) {
            return TODO;
        }

        User user = User.findByEmail(session().get("email"));
        Place place = Place.findById(id);
        String title = boundForm.data().get("title");
        String description = boundForm.data().get("description");

        Reservation r = new Reservation();
        r.place = place;
        r.user = user;
        r.title = title;
        r.description = description;
        r.status = models.Status.getStatusById(3);

        r.status = models.Status.getStatusById(1);

        r.save();
        return redirect(routes.Application.index());
    }

    public Result reservationsList() {
        return ok(reservationlist.render(Reservation.findAll(), models.Status.findAll()));
    }

    public Result changeStatus() {
        DynamicForm boundForm = Form.form().bindFromRequest();
        models.Status status = models.Status.getStatusById(Integer.parseInt(boundForm.data().get("statusId")));
        Reservation reservation = Reservation.findById(Integer.parseInt(boundForm.data().get("reservationId")));
        Logger.info(reservation.title);
        if(status == null || reservation == null) {
            return badRequest("Something went wrong");
        }
        reservation.status = status;
        reservation.update();

        return redirect(routes.ReservationController.reservationsList());
    }
}
