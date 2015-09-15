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
    public int id;
    public String serviceType;
    public static Finder<Integer, Service> finder = new Finder<>(Service.class);

    public static List<Service> findAll() {
        return finder.order("serviceType").findList();
    }

    public static Service findByType(String type) {
        return finder.where().eq("serviceType", type).findUnique();
    }
    /*
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Accommodation');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Arts&Entertainment');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Attractions');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Business');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Coffee');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Food');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Night life');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Services');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Shopping');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Sport');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Transportation');
INSERT INTO `bitNavigator`.`service` (`id`, `service_type`) VALUES (NULL, 'Other');
     */
}
