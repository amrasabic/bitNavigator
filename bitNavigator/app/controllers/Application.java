package controllers;

import models.User;
import play.*;
import play.mvc.*;

import play.data.Form;
import com.avaje.ebean.Ebean;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

}
