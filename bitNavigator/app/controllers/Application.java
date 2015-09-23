package controllers;

import com.cloudinary.Cloudinary;
import models.Image;
import models.Place;
import models.Reservation;
import models.User;
import play.Play;
import play.mvc.*;
import views.html.*;
import java.util.List;

public class Application extends Controller {

    public Result index() {
        List<Place> places = Place.findAll();
        User user = User.findByEmail(session().get("email"));
        models.Status status = models.Status.getStatusById(2);
        List<Reservation> reservations = Reservation.findByStatus(user, status);
        Image.cloudinary = new Cloudinary("cloudinary://"+ Play.application().configuration().getString("cloudinary.string"));
        for(int i = 0; i < reservations.size(); i++) {
            if(reservations.size() == 1) {
                flash("warning", "There is " + reservations.size() + " reservation with status 'waiting'!");
            } else if(reservations.size() > 1){
                flash("warning", "There are " + reservations.size() + " reservations with status 'waiting'!");
            }
        }

        return ok(index.render(places));
    }

}
