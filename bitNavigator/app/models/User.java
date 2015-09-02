package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.data.validation.Constraints;

import play.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ognjen.cetkovic on 01/09/15.
 */
@Entity
public class User extends Model {

    public static Finder<Integer, User> finder = new Finder<>(Integer.class, User.class);

    @Id
    public int id;
    public String username;
    @Constraints.Required
    public String email;
    public String firstName;
    public String lastName;
    @Constraints.Required
    public String password;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static User findByEmail(String email) {
        User u = finder.where().eq("email", email).findUnique();
        //Logger.info(u.email);

        return u;
    }
}
