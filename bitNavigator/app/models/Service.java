package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by ognje on 09-Sep-15.
 */
@Entity
public class Service extends Model {

    @Id
    public Integer id;
    public String serviceType;
    public boolean isReservable;

    public static Finder<Integer, Service> finder = new Finder<>(Service.class);

    public static List<Service> findAll() {
        return finder.order("serviceType").findList();
    }

    public static Service findByType(String type) {
        return finder.where().eq("serviceType", type).findUnique();
    }

    public Boolean isReservable(){
        if(isReservable){
            return true;
        } else {
            return false;
        }
    }
    /*
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Accommodation', 1);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Arts&Entertainment', 0);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Attractions', 0);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Business', 0);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Coffee', 1);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Food', 1);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Night life', 1);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Services', 0);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Shopping', 0);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Sport', 1);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Transportation', 1);
    INSERT INTO `bitNavigator`.`service` (`id`, `service_type`,`is_reservable`) VALUES (NULL, 'Other', 0);
    */
}
