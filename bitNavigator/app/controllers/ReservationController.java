package controllers;

import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import models.Status;

import play.mvc.Security;
import utillities.Authenticators;
import views.html.place.*;

/**
 * Created by Amra on 9/17/2015.
 */
public class ReservationController extends Controller {

    private static final Form<Reservation> reservationForm = Form.form(Reservation.class);

    @Security.Authenticated(Authenticators.User.class)
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
        if(title == null || description == null) {
            // nesto ?
        } else {
            r.title = title;
            r.description = description;
        }
        r.status = models.Status.getStatusById(3);

        r.save();
        return redirect(routes.Application.index());
    }
}
