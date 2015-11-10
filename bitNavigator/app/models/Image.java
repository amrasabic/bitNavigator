package models;

import com.avaje.ebean.Model;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import play.Logger;
import play.mvc.Security;
import utillities.Authenticators;
import utillities.SessionHelper;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Amra on 9/8/2015.
 */

//This class represents image model
@Entity
public class Image extends Model {

    //atributes

    public static Finder<Integer, Image> finder = new Finder<>(Image.class);
    public static Cloudinary cloudinary;

    @Id
    public Integer id;
    public String public_id;
    public String image_url;
    public String secret_image_url;
    public boolean isPublished;
    @ManyToOne (cascade = CascadeType.PERSIST)
    public Place place;
    @OneToOne (cascade = CascadeType.PERSIST)
    public User user;

    /**
     * Default constructor.
     */
    public Image() {

    }

    //Finds all images by place
    public static List<Image> findByPlace(Place place) {
        return finder.where().eq("place", place).findList();
    }

    //Creates and saves image
    public static Image create(String public_id, String image_url, String secret_image_url) {
        Image i = new Image();
        i.public_id = public_id;
        i.image_url = image_url;
        i.secret_image_url = secret_image_url;
        i.save();
        return i;
    }

    //Finds image by user
    public static Image findByUser(User user) {
        return finder.where().eq("user", user).findUnique();
    }

    /**
     * Adds image to place and saves it
     * @param file image file
     * @param place place on which you are adding image
     */
    public static void addImage(File file, Place place) {
        Map uploadResult;
        try {
            uploadResult = cloudinary.uploader().upload(file, null);
        } catch (IOException e) {
            Logger.error(e.getStackTrace().toString());
            return;
        }

        Image image = new Image();

        image.public_id = (String) uploadResult.get("public_id");
        image.image_url = (String) uploadResult.get("url");
        image.secret_image_url = (String) uploadResult.get("secure_url");

        image.place = place;

        image.save();
    }

    /**
     * Adds avatar image to current user
     * @param image image that is uploaded
     * @return avatar image
     */
    @Security.Authenticated(Authenticators.User.class)
    public static Image createAvatar(File image) {
        Map uploadResult;
        try {
            uploadResult = cloudinary.uploader().upload(image, null);
        } catch (IOException e) {
            Logger.error(e.getStackTrace().toString());
            return null;
        }
        User user = SessionHelper.getCurrentUser();

        if(Image.findByUser(user) != null) {
            Image.findByUser(user).deleteImage();
            Image.findByUser(user).delete();

        }
        Image avatar = new Image();

        avatar.public_id = (String) uploadResult.get("public_id");
        avatar.image_url = (String) uploadResult.get("url");
        avatar.secret_image_url = (String) uploadResult.get("secure_url");

        avatar.user = SessionHelper.getCurrentUser();

        avatar.save();
        return avatar;
    }

    //Finds image by id
    public static Image findImageById(Integer id){
        return Image.finder.where().eq("id", id).findUnique();
    }

    //Getts list of all images
    public static List<Image> all() {
        return finder.all();
    }

    //Getts size of width and height you want
    public String getSize(int width, int height) {
        String url = cloudinary.url().format("jpg")
                .transformation(new Transformation().width(width).height(height).crop("fill"))
                .generate(public_id);
        return url;
    }

    //Crops picture to thumbnail size
    public String getThumbnail(){
        String url = cloudinary.url().format("png")
                .transformation(
                        new Transformation().width(150).height(150).crop("thumb").gravity("face").radius("max")
                )
                .generate(public_id);
        return url;
    }

    //Deletes image
    public void deleteImage() {
        try {
            cloudinary.uploader().destroy(public_id, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}