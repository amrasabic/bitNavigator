package controllers;

import com.avaje.ebean.Ebean;
import models.*;
import play.Play;
import play.mvc.Controller;

import play.mvc.Result;
import views.html.*;
import views.html.place.*;
import play.data.Form;
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
import views.html.place.addplace;

import static views.html.place.addplace.*;

/**
 * Created by ognjen.cetkovic on 08/09/15.
 */
public class PlaceController extends Controller{

    private static final Form<Place> placeForm = Form.form(Place.class);
    private static final Form<Image> imageForm = Form.form(Image.class);
    private static final Form<Service> serviceForm = Form.form(Service.class);
    private static final Form<Comment> commentForm = Form.form(Comment.class);
    private static List<String> imageLists = new ArrayList<>();

    public Result addPlace() {
        return ok(addplace.render(placeForm, Service.findAll()));
    }


    public Result savePlace() {

        Form<Place> boundForm = placeForm.bindFromRequest();
        Form<Service> boundServiceForm = serviceForm.bindFromRequest();

        if (boundForm.hasErrors() || boundServiceForm.hasErrors()) {
            flash("error", "Wrong input!");
            return badRequest(addplace.render(boundForm, Service.findAll()));
        }

        Service service = boundServiceForm.get();
        service = Service.findByType(service.serviceType);

        if (service == null) {
            flash("error", "Must add service!");
            return badRequest(addplace.render(boundForm, Service.findAll()));
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String formatted = format1.format(cal.getTime());

        Place place = boundForm.get();
        place.user = User.findByEmail(session("email"));
        place.placeCreated = Calendar.getInstance();
        place.service = service;
        place.save();


        MultipartFormData body = request().body().asMultipartFormData();
        List<FilePart> pictures = body.getFiles();

        if (pictures != null) {
            for (FilePart picture : pictures) {
                String name = place.title + formatted;
                File file = picture.getFile();
                String path = Play.application().path() + "/public/images/placeImages/" + place.title + "/" + name;

                Logger.info(name);
                try {
                    FileUtils.moveFile(file, new File(path));
                    imageLists.add(name);
                    Image image = new Image();
                    image.name = name;
                    path ="/images/placeImages/" + place.title + "/" + name;
                    image.path = path;
                    image.place = place;
                    image.save();

                } catch (IOException ex) {
                    Logger.info("Could not move file. " + ex.getMessage());
                    flash("error", "Could not move file.");
                }
            }

            return ok(index.render(Place.findAll()));
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
        return redirect(routes.PlaceController.placeList());
    }

    public Result editPlace(int id){
        Place place = Place.findById(id);
        if (place == null) {
            return notFound(String.format("Place %s does not exists.", id));
        }
        Form <Place> filledForm =  placeForm.fill(place);
        List<Service> services = Service.findAll();
        List<Comment> comments = Comment.findAll();
        return ok(editplace.render(place, services, comments));
    }

    public Result viewPlace(int id){
        Place place = Place.findById(id);
        if (place == null) {
            return notFound(String.format("Place %s does not exists.", id));
        }
        List<Service> services = Service.findAll();
        List<Comment> comments = Comment.findByPlace(place);
        return ok(viewplace.render(place, services, comments, Image.findByPlace(place)));
    }

    public Result postComment(int id) {
        Form<Comment> boundForm = commentForm.bindFromRequest();

        if (boundForm.hasErrors()) {
            return unauthorized("Can not post an emnpty comment!");
        }

        Comment comment = boundForm.get();
        User user = User.findByEmail(session("email"));
        if(user == null) {
            return TODO;
        }
        comment.user = user;
        Place place = Place.findById(id);
        if (place == null) {
            return TODO;
        }
        comment.place = place;
        comment.commentCreated = Calendar.getInstance();
        if(comment.rate == 0) {
            comment.rate = null;
        }
        comment.save();
        List<Service> services = Service.findAll();
        List<Comment> comments = Comment.findAll();
        return ok(viewplace.render(place, services, comments, Image.findByPlace(place)));
    }

    public Result updateComment(int id) {
        Form<Comment> boundForm = commentForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            return unauthorized("Can not post an emnpty comment!");
        }

        Comment comment = Comment.findById(id);
        comment.commentContent = boundForm.bindFromRequest().field("commentContent").value();
        comment.rate = Integer.parseInt(boundForm.bindFromRequest().field("rate").value());

        comment.commentCreated = Calendar.getInstance();
        if(comment.rate == 0) {
            comment.rate = null;
        }
        comment.update();
        List<Service> services = Service.findAll();
        List<Comment> comments = Comment.findAll();
        return ok(viewplace.render(comment.place, services, comments, Image.findByPlace(comment.place)));
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
