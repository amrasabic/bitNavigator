import models.Image;
import play.Application;
import play.GlobalSettings;
import play.Play;
import play.libs.F;
import play.mvc.Result;
import play.mvc.Http;
import play.mvc.Http.*;
import com.cloudinary.Cloudinary;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.notFound;
/**
 * Created by ognjen on 29-Sep-15.
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {
        super.onStart(application);
        Image.cloudinary = new Cloudinary("cloudinary://"+ Play.application().configuration().getString("cloudinary.string"));
    }

    @Override
    public F.Promise<Result> onHandlerNotFound(Http.RequestHeader requestHeader) {
        return F.Promise.<Result>pure(notFound(views.html.helpers.notfound.render()));
    }

    @Override
    public F.Promise<Result> onBadRequest(RequestHeader request, String error) {
        return F.Promise.<Result>pure(badRequest(views.html.helpers.notfound.render()));
    }
}
