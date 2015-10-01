package controllers;

import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import models.Status;

import play.mvc.Security;
import utillities.Authenticators;
import utillities.SessionHelper;
import views.html.place.*;
import views.html.reservations.reservationlist;

import play.Logger;

import java.util.Calendar;

/**
 * Created by amra.sabic on 9/17/2015.
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
        String reservationDay = boundForm.data().get("reservationDay");
        String content = boundForm.data().get("content");

        Reservation r = new Reservation();
        r.place = place;
        r.user = user;
        Message message = new Message();
        message.sender = SessionHelper.getCurrentUser();
        if(content == null) {
            // nesto >?
        } else {
            message.content = content;
        }
        r.reservationCreated = Calendar.getInstance();
        r.reservationDay = reservationDay;
        r.status = models.Status.getStatusById(models.Status.WAITING);

        r.messages.add(message);
        r.status = models.Status.getStatusById(models.Status.WAITING);
        r.save();
        message.messageCreated = Calendar.getInstance();
        message.reservation.id = r.id;
        message.save();
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
       // Logger.info(reservation.title);
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
        if(reservation.status.id == models.Status.WAITING) {
            reservation.delete();
        }

        return redirect(routes.ReservationController.reservationsList());
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result updateReservation(Integer id) {

        Form<Reservation> boundForm = reservationForm.bindFromRequest();

        Reservation reservation = Reservation.findById(id);

//        reservation.title = boundForm.bindFromRequest().field("rtitle").value();
//        reservation.description = boundForm.bindFromRequest().field("rdescription").value();

//        Logger.info(reservation.description);
//        Logger.info(reservation.title);
        reservation.update();
        return redirect(routes.ReservationController.reservationsList());
    }
}
