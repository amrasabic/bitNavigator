package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by ognjen on 09-Sep-15.
 */
@Entity
public class Service extends Model {

    @Id
    public Integer id;
    @Column (unique = true)
    public String serviceType;
    public String serviceIcon;
    public boolean isReservable;

    public static Finder<Integer, Service> finder = new Finder<>(Service.class);

    public static List<Service> findAll() {
        return finder.order("serviceType").findList();
    }

    public static Service findByType(String type) {
        return finder.where().eq("serviceType", type).findUnique();
    }

    public static Service findById(int id) {
        return finder.byId(id);
    }

    public Boolean isReservable(){
        if(isReservable){
            return true;
        } else {
            return false;
        }
    }

    /*
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Accommodation', 1, 'images/serviceImages/accommodation.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Arts&Entertainment', 0, 'images/serviceImages/arts.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Attractions', 0, 'images/serviceImages/attractions.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Business', 0, 'images/serviceImages/business.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Coffee', 1, 'images/serviceImages/coffe.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Food', 1, 'images/serviceImages/food.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Night life', 1, 'images/serviceImages/nightlife.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Services', 0, 'images/serviceImages/services.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Shopping', 0, 'images/serviceImages/shopping.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Sport', 1, 'images/serviceImages/sport.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Transportation', 1, 'images/serviceImages/transportation.png');
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`, `service_icon`) VALUES (NULL, 'Other', 0, 'images/serviceImages/other.png');
    */
}
