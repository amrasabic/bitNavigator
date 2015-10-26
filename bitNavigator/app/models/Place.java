package models;

import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.*;

/**
 * Class representing Place model.
 */
@Entity
public class Place extends Model {

    // declaration of parameters
    @Id
    public Integer id;
    @Constraints.Required(message = "This field is required!")
    @Constraints.MaxLength(value = 150, message = "Title can not hold more than 150 characters!")
    public String title;
    @Column(columnDefinition = "TEXT")
    public String description;
    public Double longitude;
    public Double latitude;
    public String address;
    public Calendar placeCreated;
    public Integer numOfViews;
    public Integer numOfReservations;
    @ManyToOne(cascade = CascadeType.PERSIST)
    public User user;
    @ManyToOne
    public Service service;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Image> images;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Comment> comments;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Reservation> reservations;
    @OneToMany(cascade = CascadeType.ALL)
    public List<ClientIP> clientIPs;

    public static Finder<Integer, Place> finder = new Finder<>(Place.class);
    /**
     * Default constructor.
     */
    public Place() {
    }

    public static List<Place> findAll() {
        return finder.all();
    }

    public static Place findById(int id) {
        return finder.byId(id);
    }

    public static Place findByTitle(String title) {
        return finder.where().eq("title", title).findUnique();
    }

    public static List<Place> findByValueInTitle(String value) {
        return finder.where().contains("title", value).findList();
    }

    public static List<Place> findByValue(String value) {
        ExpressionFactory exprFactory = finder.getExpressionFactory();
        return finder.where().or(exprFactory.contains("title", value), exprFactory.contains("address", value)).findList();
    }

    public static List<Place> findByUser(User user) {
        return finder.where().eq("user", user).findList();
    }

    public static List<Place> findByService(Service serv, Integer id) {
        List<Place> list = finder.where().eq("service", serv).findList();
        Place p = Place.findById(id);
        list.remove(p);
        sortByRating(list);
        return list;
    }

    public static void sortByRating(List<Place> places){
        Collections.sort(places, new Comparator<Place>() {
            @Override
            public int compare(Place o1, Place o2) {
                if(o1.getRating()==null){
                    if (o2.getRating() == null)
                        return 0;
                    return 1;
                }else if(o2.getRating()==null){
                    return -1;
                }else if(o1.getRating()>o2.getRating()){
                    return -1;
                }else if(o2.getRating()>o1.getRating()){
                    return 1;
                }
                return 0;
            }
        });
    }

    public Double getRating() {
        List<Comment> comments = Comment.findByPlace(this);
        double rating = 0;
        int counter = 0;
        for (Comment comment : comments) {
            if (comment.rate != null) {
                counter++;
                rating += comment.rate;
            }
        }
        if (counter > 0) {
            return rating / counter;
        }
        return null;
    }

    public void updateNumOfViews(){
        if(this.numOfViews == null){
            this.numOfViews = 0;
        }
        this.numOfViews++;
        this.update();
    }

    public List<Integer> getListRating(){
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        return list;
    }

    @Override
    public void delete() {
        WorkingHours.findByPlace(this).delete();
        super.delete();
    }
}