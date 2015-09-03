package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.data.validation.Constraints;

import play.Logger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ognjen.cetkovic on 01/09/15.
 */
@Entity
public class User extends Model {

    public static Finder<Integer, User> finder = new Finder<>(Integer.class, User.class);

    @Id
    public int id;
    @Constraints.Required
    public String email;
    public String firstName;
    public String lastName;
    @Constraints.Required
    public String password;
    @Formats.DateTime(pattern="dd/MM/yyyy")
    public Calendar accountCreated;

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static User findByEmail(String email) {
        return finder.where().eq("email", email).findUnique();
    }
}
