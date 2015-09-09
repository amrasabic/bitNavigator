package controllers;

import com.avaje.ebean.Ebean;
import models.Image;
import models.Place;
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
import java.util.List;
import org.apache.commons.io.FileUtils;
import play.Logger;

/**
 * Created by ognjen.cetkovic on 08/09/15.
 */
public class PlaceHandler extends Controller{

    private static final Form<Place> placeForm = Form.form(Place.class);
    private static final Form<Image> imageForm = Form.form(Image.class);
    private static List<String> imageLists = new ArrayList<>();

    public Result addPlace() {
        return ok(addplace.render(placeForm));
    }


    public Result savePlace() {

        /*Form<Place> boundForm = placeForm.bindFromRequest();

        if (boundForm.hasErrors()) {
            return TODO;
        }
*/
        MultipartFormData body = request().body().asMultipartFormData();
        List<FilePart> pictures = body.getFiles();

        Form<Image> bound = imageForm.bindFromRequest();
        Image image = bound.get();

        if (pictures != null) {
            for (FilePart picture : pictures) {
                String name = picture.getFilename();
                File file = picture.getFile();
                String path = Play.application().path() + "\\public\\images\\" + name;

                try {
                    FileUtils.moveFile(file, new File(path));
                    imageLists.add(name);
                } catch (IOException ex) {
                    Logger.info("Could not move file. " + ex.getMessage());
                    flash("error", "Could not move file.");
                }
            }
            Ebean.save(image);
            return ok("File uploaded");
        } else {
            flash("error", "Files not present.");
            return badRequest("Pictures missing.");
        }



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
