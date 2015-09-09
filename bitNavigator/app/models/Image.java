package models;

import play.db.ebean.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Created by Amra on 9/8/2015.
 */
@Entity
public class Image extends Model {

    public static Finder<Integer, Image> finder = new Finder<>(Integer.class, Image.class);

    @Id
    public Integer id;
    public String name;
    public String path;
    @OneToMany
    public Place place;

    /**
     * Default constructor.
     */
    public Image() {

    }


}