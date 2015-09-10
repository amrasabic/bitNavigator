package controllers;

import models.Place;
import models.User;
import play.*;
import play.mvc.*;

import play.data.Form;
import com.avaje.ebean.Ebean;

import views.html.*;

import java.util.List;

public class Application extends Controller {

    public Result index() {
        List<Place> places = Place.findAll();
        return ok(index.render(places));
    }

}
