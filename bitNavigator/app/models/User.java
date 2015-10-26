package models;

import com.avaje.ebean.Model;
import controllers.UserController;
import play.Logger;
import play.data.validation.Constraints;
import utillities.PasswordHash;
import utillities.SessionHelper;

import javax.persistence.*;
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
    public PhoneNumber phoneNumber;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Place> places;
    private boolean admin = false;
    @Column(unique = true)
    private String token;
    private boolean validated = false;
    @OneToMany (cascade = CascadeType.ALL)
    public List<Comment> comments;

    public static final Finder<Long, User> find = new Finder<>(
            User.class);

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

    public static User newUser(UserController.SignUpForm signUp) {
        User user = new User();
        user.email = signUp.email;
        user.firstName = signUp.firstName;
        user.lastName = signUp.lastName;
        try {
            signUp.password = PasswordHash.createHash(signUp.password);
        } catch (Exception e) {
            Logger.error("Could not create hash");
        }
        user.password = signUp.password;
        user.accountCreated = Calendar.getInstance();
        user.save();
        return user;
    }

    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
        save();
    }

    public boolean isAdmin() {
        return admin;
    }

    public static User updateUser(UserController.UserNameForm userNameForm){

        User user = SessionHelper.getCurrentUser();

        user.firstName = userNameForm.firstName;
        user.lastName = userNameForm.lastName;

        return  user;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public static User findUserByToken(String token) {
        return find.where().eq("token", token).findUnique();
    }

    public static boolean validateUser(User user) {
        if (user == null) {
            return false;
        }
        user.setToken(null);
        user.setValidated(true);
        user.update();
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        User u = (User) obj;
        return this.id == u.id;
    }

    @Override
    public void delete() {
        for (Comment comment : Comment.findByUser(this)) {
            comment.delete();
        }
        for (Report report : Report.findByUser(this)) {
            report.delete();
        }
        for (Place place : Place.findByUser(this)) {
            place.delete();
        }
        for (Reservation reservation : Reservation.findByUser(this)) {
            reservation.delete();
        }
        for (Message message : Message.findBySenderAndReciever(this)) {
            message.delete();
        }
        super.delete();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
