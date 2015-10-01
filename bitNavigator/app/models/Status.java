package models;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by Amra on 9/16/2015.
 */
@Entity
public class Status {

    public static final int APPROVED = 1;
    public static final int WAITING = 2;
    public static final int DENIED = 3;

    @Id
    public Integer id;
    @Column(unique = true)
    public String status;

    public static Model.Finder<Integer, Status> finder = new Model.Finder<Integer, Status>(Status.class);

    public Status() {

    }

    public static Status findById(Integer id){
        return finder.byId(id);
    }

    public static List<Status> findAll() {
        return finder.all();
    }


    /*
    INSERT INTO `status`(`id`, `status`) VALUES (3,'Denied');
    INSERT INTO `status`(`id`, `status`) VALUES (1,'Approved');
    INSERT INTO `status`(`id`, `status`) VALUES (2,'Waiting');
    */
}
