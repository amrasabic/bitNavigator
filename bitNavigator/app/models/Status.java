package models;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Amra on 9/16/2015.
 */
@Entity
public class Status {

    @Id
    public Integer id;
    @Column(unique = true)
    public String status;

    public static Model.Finder<Integer, Status> finder = new Model.Finder<Integer, Status>(Status.class);

    public Status() {

    }

    public static Status getStatusById(Integer id){
        return finder.byId(id);
    }

    /*
    INSERT INTO `status`(`id`, `status`) VALUES (1,'DENIED');
    INSERT INTO `status`(`id`, `status`) VALUES (2,'ACCEPTED');
    INSERT INTO `status`(`id`, `status`) VALUES (3,'WAITING');
    */
}
