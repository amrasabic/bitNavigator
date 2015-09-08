package controllers;

import play.mvc.Controller;

import play.mvc.Result;
import views.html.*;

/**
 * Created by ognjen.cetkovic on 08/09/15.
 */
public class PlaceHandler extends Controller{

    public Result addPlace() {
        return ok(addplace.render());
    }

}
