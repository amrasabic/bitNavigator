package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents Service model
 */
@Entity
public class Service extends Model {
    // Declaring properties of Service model.
    @Id
    public Integer id;
    @Column (unique = true)
    @Constraints.Required(message = "This field is required!")
    public String serviceType;
    public String serviceIcon;
    public boolean isReservable;

    //Finder for service
    public static Finder<Integer, Service> finder = new Finder<>(Service.class);

    //Finds all services
    public static List<Service> findAll() {
        return finder.order("serviceType").findList();
    }

    //Finds service by type
    public static Service findByType(String type) {
        return finder.where().eq("serviceType", type).findUnique();
    }

    //Finds service by id
    public static Service findById(int id) {
        return finder.byId(id);
    }

    //Checks if service is reservable
    public Boolean isReservable(){
        if(isReservable){
            return true;
        } else {
            return false;
        }
    }
}
