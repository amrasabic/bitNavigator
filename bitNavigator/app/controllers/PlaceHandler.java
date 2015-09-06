package controllers;

import com.avaje.ebean.Ebean;
import models.Place;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.addplace;

import play.Logger;
import java.util.Calendar;

public class PlaceHandler extends Controller {

    public static final String ERROR_MESSAGE = "error";
    public static final String SUCCESS_MESSAGE = "success";

    private static final Form<Place> placeForm = Form.form(Place.class);


    public Result addPlace () {
        return ok(addplace.render(placeForm));
    }

    public  Result save () {
        Form<Place> boundForm = placeForm.bindFromRequest();

        Place place = new Place();

        String title = boundForm.bindFromRequest().field("title").value();
        String description = boundForm.bindFromRequest().field("description").value();
        String address = boundForm.bindFromRequest().field("address").value();
        String phone = boundForm.bindFromRequest().field("phone").value();

        place.title = title;
        place.description = description;
        place.address = address;
        place.phone = phone;

        Ebean.save(place);

        return ok(addplace.render(placeForm));
    }
}
