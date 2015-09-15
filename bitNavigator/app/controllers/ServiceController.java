package controllers;

import models.Place;
import models.Service;
import play.mvc.Controller;
import play.data.Form;
import play.Play;
import play.mvc.Result;
import views.html.*;
import views.html.admin.*;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.io.FileUtils;
import play.Logger;

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

        if(service.isReservable) {
            service.isReservable = true;
        } else {
            service.isReservable = false;
        }

        MultipartFormData body = request().body().asMultipartFormData();
        List<FilePart> picture = body.getFiles();

        if (picture != null) {
            picture.get(0);
            File file = picture.get(0).getFile();

            String imagepath = Play.application().path() + "/public/images/serviceImages/" + service.serviceType + ".png";

            try {

                FileUtils.moveFile(file, new File(imagepath));

                imagepath ="/images/serviceImages/" + service.serviceType + ".png";
                service.serviceIcon = imagepath;

                service.save();

            } catch (IOException ex) {
                Logger.info("Could not move file. " + ex.getMessage());
                flash("error", "Could not move file.");
            }

        } else {
            flash("error", "Files not present.");
            return badRequest("Pictures missing.");
        }


        service.save();
        return ok(index.render(Place.findAll()));
    }

}





