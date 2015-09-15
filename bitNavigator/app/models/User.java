package models;

import javax.persistence.*;

import com.avaje.ebean.Model;
import controllers.UserController;
import play.data.validation.Constraints;

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

    public static Finder<Integer, User> finder = new Finder<>(User.class);

    @Id
    public int id;
    @Constraints.Email
    @Column (unique = true)
    @Constraints.Required
    public String email;
    @Constraints.Pattern ("[a-zA-Z]+")
    public String firstName;
    @Constraints.Pattern ("[a-zA-Z]+")
    public String lastName;
    @Constraints.MinLength (8)
    @Constraints.MaxLength (25)
    @Constraints.Required
    public String password;
    public Calendar accountCreated;
    public String phoneNumber;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Place> places;
    public boolean admin = false;
    @OneToOne
    public Image image;

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

    public static void newUser(UserController.SignUpForm signUp) {
        User user = new User();
        user.email = signUp.email;
        user.firstName = signUp.firstName;
        user.lastName = signUp.lastName;
        user.password = signUp.password;
        user.accountCreated = Calendar.getInstance();
        user.phoneNumber = signUp.phoneNumber;
        user.save();
    }

    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
        save();
    }

    public static List<User> findAll() {
        return finder.all();
    }

    /**
     * Returns User with given email or null if no account is linked to given email.
     * @param email An email.
     * @return User with given email or null if no account is linked to given email.
     */
    public static User findByEmail(String email) {
        return finder.where().eq(EMAIL, email).findUnique();
    }

}
