package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by ognje on 09-Sep-15.
 */
@Entity
public class Service extends Model {

    @Id
    public int id;
    public String type;
    public Service parent;


}
