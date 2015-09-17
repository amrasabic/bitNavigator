package models;



import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Amra on 9/8/2015.
 */
@Entity
public class Image extends Model {

    public static Finder<Integer, Image> finder = new Finder<>(Image.class);

    @Id
    public Integer id;
    public String name;
    public String path;
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
        return finder.where().eq("place", place).findList();
    }
}