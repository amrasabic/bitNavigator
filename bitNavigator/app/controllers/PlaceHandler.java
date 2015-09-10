package controllers;

import com.avaje.ebean.Ebean;
import models.Image;
import models.Place;
import models.Service;
import models.User;
import play.Play;
import play.mvc.Controller;

import play.mvc.Result;
import views.html.*;
import play.data.Form;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.io.FileUtils;
import play.Logger;

/**
 * Created by ognjen.cetkovic on 08/09/15.
 */
public class PlaceHandler extends Controller{

    private static final Form<Place> placeForm = Form.form(Place.class);
    private static final Form<Image> imageForm = Form.form(Image.class);
    private static final Form<Service> serviceForm = Form.form(Service.class);
    private static List<String> imageLists = new ArrayList<>();

    public Result addPlace() {
        List<Service> services = Service.findAll();

        return ok(addplace.render(placeForm, services));
    }


    public Result savePlace() {

        Form<Place> boundForm = placeForm.bindFromRequest();
        Form<Service> boundServiceForm = serviceForm.bindFromRequest();

        Place p = boundForm.get();


        if (boundForm.hasErrors() || boundServiceForm.hasErrors()) {
            return TODO;
        }

        Service service = boundServiceForm.get();
        service = Service.findByType(service.serviceType);

        if (service == null) {
            return TODO;
        }

        Place place = boundForm.get();
        place.user = User.findByEmail(session("email"));
        place.placeCreated = Calendar.getInstance();
        place.service = service;
        place.save();


        MultipartFormData body = request().body().asMultipartFormData();
        List<FilePart> pictures = body.getFiles();

        if (pictures != null) {
            for (FilePart picture : pictures) {
                String name = picture.getFilename();
                File file = picture.getFile();
                String path = Play.application().path() + "/public/images/placeImages/" + name;

                Logger.info(name);
                try {
                    FileUtils.moveFile(file, new File(path));
                    imageLists.add(name);
                    Image image = new Image();
                    image.name = name;
                    image.path = path;
                    image.place = place;
                    image.save();

                } catch (IOException ex) {
                    Logger.info("Could not move file. " + ex.getMessage());
                    flash("error", "Could not move file.");
                }
            }

            return ok("File uploaded");
        } else {
            flash("error", "Files not present.");
            return badRequest("Pictures missing.");
        }

    }

    public Result placeList(){
        List<Place> places = Place.findAll();
        return ok(placelist.render(places));
    }

    public Result delete(int id) {
        Place place = Place.findById(id);
        if (place == null) {
            return notFound(String.format("Place %s does not exists.", id));
        }

        User owner = User.findByEmail(session("email"));
        Logger.info(owner.email);
        Logger.info(place.user.email);
        if (owner == null || !place.user.equals(owner)) {
            if (!owner.admin) {
                return unauthorized("Permission denied!");
            }
        }


        place.delete();
        return redirect(routes.PlaceHandler.placeList());
    }

    public Result editPlace(int id){
        Place place = Place.findById(id);
        if (place == null) {
            return notFound(String.format("Place %s does not exists.", id));
        }
        Form <Place> filledForm =  placeForm.fill(place);
        List<Service> services = Service.findAll();
        return ok(editplace.render(filledForm, services));
    }

    public Result validateForm(){
        Form<Place> binded = placeForm.bindFromRequest();
        if(binded.hasErrors()){
            return badRequest(binded.errorsAsJson());
        } else {
            return ok("we good, we good");
        }
    }

}
