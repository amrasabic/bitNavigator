package controllers;

import models.*;
import org.apache.commons.io.FileUtils;
import play.Logger;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;
import utillities.Authenticators;
import views.html.index;
import views.html.place.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by ognjen.cetkovic on 08/09/15.
 */
public class PlaceController extends Controller{

    private static final Form<Place> placeForm = Form.form(Place.class);
    private static final Form<Image> imageForm = Form.form(Image.class);
    private static final Form<Service> serviceForm = Form.form(Service.class);
    private static final Form<Comment> commentForm = Form.form(Comment.class);
    private static List<String> imageLists = new ArrayList<>();

    @Security.Authenticated(Authenticators.User.class)
    public Result addPlace() {
        return ok(addplace.render(new Place(), Service.findAll()));
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result savePlace() {

        Form<Place> boundForm = placeForm.bindFromRequest();
        Form<Service> boundServiceForm = serviceForm.bindFromRequest();

        if (boundForm.hasErrors() || boundServiceForm.hasErrors()) {
            flash("error", "Wrong input!");
            return badRequest(addplace.render(boundForm.get(), Service.findAll()));
        }

        Service service = Service.findByType(boundServiceForm.get().serviceType);

        if (service == null) {
            flash("error", "Must add service!");
            return badRequest(addplace.render(boundForm.get(), Service.findAll()));
        }

        Place place = boundForm.get();
        place.user = User.findByEmail(session("email"));
        place.placeCreated = Calendar.getInstance();
        place.service = service;
        place.save();

        MultipartFormData body = request().body().asMultipartFormData();
        List<FilePart> pictures = body.getFiles();

        if (pictures != null) {
//            for (FilePart picture : pictures) {
//                File file = picture.getFile();
//                String name = formatted + file.getName();
//                String path = Play.application().path() + "/public/images/placeImages/" + place.title + "/" + name;
//
//                Logger.info(name);
//                try {
//                    FileUtils.moveFile(file, new File(path));
//                    imageLists.add(name);
//                    Image image = new Image();
//                    image.name = name;
//                    path ="/images/placeImages/" + place.title + "/" + name;
//                    image.path = path;
//                    image.place = place;
//                    image.save();
//
//                } catch (IOException ex) {
//                    Logger.info("Could not move file. " + ex.getMessage());
//                    flash("error", "Could not move file.");
//                }
//            }

            return ok(index.render(Place.findAll()));
        } else {
            flash("error", "Files not present.");
            return badRequest("Pictures missing.");
        }

    }

    @Security.Authenticated(Authenticators.User.class)
    public Result updatePlace(int id) {

        Form<Place> boundForm = placeForm.bindFromRequest();
        Form<Service> boundServiceForm = serviceForm.bindFromRequest();

        if (boundForm.hasErrors() || boundServiceForm.hasErrors()) {
            flash("error", "Wrong input!");
            return redirect(routes.PlaceController.editPlace(id));
        }

        Service service = boundServiceForm.get();
        service = Service.findByType(service.serviceType);

        if (service == null) {
            flash("error", "Must add service!");
            return redirect(routes.PlaceController.editPlace(id));
        }

        Place place = boundForm.get();
        place.user = User.findByEmail(session("email"));
        place.placeCreated = Calendar.getInstance();
        place.service = service;
        if(Place.findById(id) == null) {
            return internalServerError("Something went wrong");
        }
        place.id = id;
        place.update();

        MultipartFormData body = request().body().asMultipartFormData();
        List<FilePart> pictures = body.getFiles();

        if (pictures != null) {
//            for (FilePart picture : pictures) {
//                File file = picture.getFile();
//                String name = formatted + file.getName();
//                String path = Play.application().path() + "/public/images/placeImages/" + place.title + "/" + name;
//
//                Logger.info(name);
//                try {
//                    FileUtils.moveFile(file, new File(path));
//                    imageLists.add(name);
//                    Image image = new Image();
//                    image.name = name;
//                    path ="/images/placeImages/" + place.title + "/" + name;
//                    image.path = path;
//                    image.place = place;
//                    image.save();
//
//                } catch (IOException ex) {
//                    Logger.info("Could not move file. " + ex.getMessage());
//                    flash("error", "Could not move file.");
//                }
//            }

            return redirect(routes.PlaceController.viewPlace(id));
        } else {
            flash("error", "Files not present.");
            return badRequest("Pictures missing.");
        }
    }

    public Result placeList(){
        List<Place> places = Place.findAll();
        return ok(placelist.render(places));
    }

    @Security.Authenticated(Authenticators.User.class)
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

    @Security.Authenticated(Authenticators.User.class)
    public Result editPlace(int id){
        Place place = Place.findById(id);
        if (place == null) {
            return notFound(String.format("Place %s does not exists.", id));
        }
  //      Form <Place> filledForm =  placeForm.fill(place);
        List<Service> services = Service.findAll();
        List<Comment> comments = Comment.findAll();
        return ok(editplace.render(place, services, comments));
    }

    public Result viewPlace(int id){
        Place place = Place.findById(id);
        if (place == null) {
            return notFound(String.format("Place %s does not exists.", id));
        }
        return ok(viewplace.render(place, Service.findAll(), Comment.findByPlace(place), Image.findByPlace(place)));
    }

    @Security.Authenticated(Authenticators.User.class)
    public Result postComment(int id) {
        Form<Comment> boundForm = commentForm.bindFromRequest();

        if (boundForm.hasErrors()) {
            return unauthorized("Can not post an empty comment!");
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
        return redirect(routes.PlaceController.viewPlace(comment.place.id));
    }

    @Security.Authenticated(Authenticators.User.class)
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
        return redirect(routes.PlaceController.viewPlace(comment.place.id));
    }

    public Result validateForm(){
        Form<Place> boundPlaceForm = placeForm.bindFromRequest();
        Form<Service> boundServiceForm = serviceForm.bindFromRequest();
        if(boundPlaceForm.hasErrors()){
            return badRequest(boundPlaceForm.errorsAsJson());
        } else if (boundServiceForm.hasErrors()) {
            return badRequest(boundServiceForm.errorsAsJson());
        } else {
            return ok();
        }
    }

}
