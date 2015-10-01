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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        String reservationDay = boundForm.data().get("reservationDay");
        String reservationtime = boundForm.data().get("reservationTime");
        SimpleDateFormat myDate = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        Calendar date = new GregorianCalendar();
        try {
            date.setTime(myDate.parse(reservationDay + " " + reservationtime));
        }catch (ParseException e){
            return TODO;
        }
        Reservation r = new Reservation();
        r.place = place;
        r.user = user;
        if(title == null || description == null) {
            return TODO;
        } else {
            r.title = title;
            r.description = description;
            r.timestamp = Calendar.getInstance();
        }
        r.status = models.Status.getStatusById(models.Status.WAITING);
        r.reservationDate = date;

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
        if(reservation.status.id == models.Status.WAITING) {
            reservation.delete();
        }

        return redirect(routes.ReservationController.reservationsList());
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result getWorkingHours() {
        DynamicForm form = Form.form().bindFromRequest();
        Place place = Place.findById(Integer.parseInt(form.get("placeId")));
        int dayOfWeek = Integer.parseInt(form.get("dayOfWeek"));
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        String response = "";

        Integer openingTime = WorkingHours.getOpeningTime(place, dayOfWeek);
        Integer closingTime = WorkingHours.getClosingTime(place, dayOfWeek);
        if(openingTime != null || closingTime != null) {
            response = String.format("%02d:%02d:00-%02d:%02d:00", openingTime / 60, openingTime % 60, closingTime / 60, closingTime % 60);
        }else{
            response = "not working";
        }

        return ok(response);
    }

}
