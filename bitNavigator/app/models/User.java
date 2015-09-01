package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
/**
 * Created by ognjen.cetkovic on 01/09/15.
 */
@Entity
public class User extends Model {

    @Id
    public int id;
    public String username;
    public String email;
    public String firstName;
    public String lastName;
    public String password;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
