package models;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Sehic on 12.11.2015.
 */
@Entity
public class RestToken extends Model {

    @Id
    public Long id;

    @Column(nullable = false)
    public String token;


    public static Finder<String, RestToken> finder = new Finder<>(RestToken.class);

    public static RestToken findRestToken(String token){
        return finder.where().eq("token", token).findUnique();
    }
}
