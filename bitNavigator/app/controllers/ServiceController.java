package controllers;

import models.Place;
import models.Service;
import play.mvc.Controller;
import play.data.Form;
import play.mvc.Result;
import views.html.*;
import views.html.admin.*;

/**
 * Created by ognjen.cetkovic on 11/09/15.
 */
public class ServiceController extends Controller {

    private static final Form<Service> serviceForm = Form.form(Service.class);

    public Result addService() {
        return ok(addservice.render());
    }

    public Result save() {
        Form<Service> boundForm = serviceForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            return TODO;
        }
        Service service = boundForm.get();
        service.save();
        return ok(index.render(Place.findAll()));
    }

}
