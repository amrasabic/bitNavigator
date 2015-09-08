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
 * Class representing User model.
 * Created by ognjen.cetkovic on 01/09/15.
 */
@Entity
public class User extends Model {

    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PASSWORD = "password";
    public static final String ACCOUNT_CREATED = "accountCreated";

    public static Finder<Integer, User> finder = new Finder<>(Integer.class, User.class);

    @Id
    public int id;
    @Constraints.Required
    public String email;
    public String firstName;
    public String lastName;
    @Constraints.Required
    public String password;
    public Calendar accountCreated;

    /**
     * Default constructor.
     */
    public User() {

    }

    /**
     * @param email An email.
     * @param password A password.
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Returns User with given email or null if no account is linked to given email.
     * @param email An email.
     * @return User with given email or null if no account is linked to given email.
     */
    public static User findByEmail(String email) {
        return finder.where().eq(EMAIL, email).findUnique();
    }

    /**
     * Returns User with given id or null if no user with that id
     * @param id - User id
     * @return User with given id
     */
    public static User findById (Integer id) {
        return finder.where().eq(ID, id).findUnique();
    }
}
