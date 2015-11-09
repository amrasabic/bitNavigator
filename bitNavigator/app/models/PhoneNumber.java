package models;

import com.avaje.ebean.Model;
import utillities.SMS;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Random;

/**
 * Created by ognjen.cetkovic on 19/10/15.
 */
//This class represents PhoneNumber model
@Entity
public class PhoneNumber extends Model{

    //atributes
    @Id
    private int id;
    private String number;
    private int token;
    private boolean validated;
    private boolean tokenSent;
    @ManyToOne (cascade = CascadeType.PERSIST)
    private User user;

    //finder for phone number
    public static final Finder<Integer, PhoneNumber> finder = new Finder<>(PhoneNumber.class);


    //Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getToken() {
        return token;
    }

    public void setToken() {
        this.token = new Random().nextInt(9000) + 1000;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public boolean isTokenSent() {
        return tokenSent;
    }

    public void setTokenSent(boolean tokenSent) {
        this.tokenSent = tokenSent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * This method return list of phone numbers by user
     * @param user user from which we want to get list of phone numbers
     * @return list of phone numbers
     */
    public static List<PhoneNumber> findByUser(User user) {
        return finder.where().eq("user", user).findList();
    }

    /**
     * Finds phone number by id
     * @param id id of phone number
     * @return phone number
     */
    public static PhoneNumber findById(int id) {
        return finder.byId(id);
    }

    /**
     * Finds phone number by number and user
     * @param number number of phone
     * @param user user by we want to find phone number
     * @return phone number
     */
    public static PhoneNumber findByNumberAndUser(String number, User user) {
        return finder.where().eq("number", number).eq("user", user).findUnique();
    }

    //Sends verification code which is needed to verify phone number
    public void sendToken() {
        SMS.sendSMS(getNumber(), "Code: " + getToken());
    }

}
