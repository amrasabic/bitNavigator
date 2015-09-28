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
import views.html.reservations.reservationlist;

import play.Logger;

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
        r.status = models.Status.getStatusById(models.Status.WAITING);

        r.save();
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result reservationsList() {
        return ok(reservationlist.render(Reservation.findAll(), models.Status.findAll()));
    }

    @Security.Authenticated(Authenticators.User.class)
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

    public Result validateForm(){
        //get the form data from the request - do this only once
        Form<Reservation> binded = reservationForm.bindFromRequest();
        //if we have errors just return a bad request
        if(binded.hasErrors()){
            flash("error", "check your inputs");
            return badRequest(binded.errorsAsJson());
        } else {
            //get the object from the form, for revere take a look at someForm.fill(myObject)
            Reservation unf = binded.get();

            flash("success", "user edited");
            return redirect("/");
        }
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result delete(Integer id){
        Reservation reservation = Reservation.findById(id);
        if(reservation.status.id == 2) {
            reservation.delete();
        }

        return redirect(routes.ReservationController.reservationsList());
    }
}
