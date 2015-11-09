package models;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by Amra on 9/16/2015.
 */

//Status model
@Entity
public class Status {

    //Status constants
    public static final int APPROVED = 1;
    public static final int WAITING = 2;
    public static final int DENIED = 3;

    //atributes
    @Id
    public Integer id;
    @Column(unique = true)
    public String status;

    public static Model.Finder<Integer, Status> finder = new Model.Finder<Integer, Status>(Status.class);

    public Status() {

    }

    //Finds status by id
    public static Status findById(Integer id){
        return finder.byId(id);
    }

    //Finds all status
    public static List<Status> findAll() {
        return finder.all();
    }

}
