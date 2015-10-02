package controllers;

import com.cloudinary.Cloudinary;
import models.Image;
import models.Place;
import models.Reservation;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utillities.SessionHelper;
import views.html.index;

import play.Play;
import play.mvc.*;
import views.html.*;
import java.util.List;

public class Application extends Controller {

    public Result index() {
        DynamicForm form = Form.form().bindFromRequest();
        String srchTerm = form.data().get("srch-term");
        List<Place> places = Place.findAll();
        if(srchTerm != null) {
            places = Place.findByValue(srchTerm);
        }
        return ok(index.render(places));
    }

}
