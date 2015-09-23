package models;

import com.avaje.ebean.Model;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import controllers.UserController;
import controllers.UserController.UserNameForm;
import play.Logger;
import play.data.Form;
import play.mvc.Security;
import utillities.Authenticators;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Amra on 9/8/2015.
 */
@Entity
public class Image extends Model {

    public static Finder<Integer, Image> find = new Finder<>(Image.class);

    @Id
    public Integer id;
    public String public_id;
    public String image_url;
    public String secret_image_url;
    public boolean isPublished;
    public static Cloudinary cloudinary;
    @ManyToOne
    public Place place;
    @OneToOne
    public User user;

    /**
     * Default constructor.
     */
    public Image() {

    }

    public static List<Image> findByPlace(Place place) {
        return find.where().eq("place", place).findList();
    }

    public static Image create(String public_id, String image_url, String secret_image_url) {
        Image i = new Image();
        i.public_id = public_id;
        i.image_url = image_url;
        i.secret_image_url = secret_image_url;
        i.save();
        return i;
    }

    public static Image create(File image) {
        Map result;
        try {
            result = cloudinary.uploader().upload(image, null);
            return create(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    @Security.Authenticated(Authenticators.User.class)
    public static Image create(Map uploadResult) {
        Image i = new Image();
        Form<UserNameForm> boundForm = Form.form(UserNameForm.class).bindFromRequest();
        User u = User.findByEmail(boundForm.bindFromRequest().field("email").value());

        if( u.avatar != null) {
            Image image = findImageById(u.avatar.id);
            u.avatar = null;
            u.update();
            image.delete();
        }

        i.public_id = (String) uploadResult.get("public_id");
        Logger.debug(i.public_id);
        i.image_url = (String) uploadResult.get("url");
        Logger.debug(i.image_url);
        i.secret_image_url = (String) uploadResult.get("secure_url");
        Logger.debug(i.secret_image_url);

        u.avatar = i;
      //  i.save();
        return i;
    }

    public static Image findImageById(Integer id){
        return Image.find.where().eq("id", id).findUnique();
    }

    public static List<Image> all() {
        return find.all();
    }

    public String getSize(int width, int height) {

        String url = cloudinary.url().format("jpg")
                .transformation(new Transformation().width(width).height(height).crop("fit").effect("sepia"))
                .generate(public_id);
        return url;
    }

    public String getThumbnail(){
        String url = cloudinary.url().format("png")
                .transformation(
                        new Transformation().width(150).height(150).crop("thumb").gravity("face").radius("max")
                )
                .generate(public_id);
        return url;
    }

    public void deleteImage() {
        try {
            cloudinary.uploader().destroy(public_id, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}