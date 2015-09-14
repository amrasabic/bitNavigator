package controllers;

import models.Place;
import play.mvc.*;
import views.html.*;
import java.util.List;

public class Application extends Controller {

    public Result index() {
        List<Place> places = Place.findAll();
        return ok(index.render(places));
    }

}
